package com.rescuehub.rescuehubserver.config

import com.rescuehub.rescuehubserver.security.*
import com.rescuehub.rescuehubserver.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository
import com.rescuehub.rescuehubserver.security.oauth2.OAuth2AuthenticationFailureHandler
import com.rescuehub.rescuehubserver.security.oauth2.OAuth2AuthenticationSuccessHandler
import com.rescuehub.rescuehubserver.security.oauth2.CustomOAuth2UserService

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
class SecurityConfig(
    @Autowired val userDetailsService: CustomUserDetailsService,

    @Autowired val oauth2UserService: CustomOAuth2UserService,

    @Autowired val successHandler: OAuth2AuthenticationSuccessHandler,

    @Autowired val failureHandler: OAuth2AuthenticationFailureHandler,

    @Autowired val cookieAuthReqRepo: HttpCookieOAuth2AuthorizationRequestRepository,

    @Autowired val tokenProvider: TokenProvider,

    @Autowired val appProperties: AppProperties

) : WebSecurityConfigurerAdapter() {

    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter =
        TokenAuthenticationFilter(tokenProvider, userDetailsService)

    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder): Unit {
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity): Unit {
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .csrf()
                .disable()
            .formLogin()
                .disable()
            .httpBasic()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(RestAuthenticationEntryPoint())
                .and()
            .authorizeRequests()
                .antMatchers("/",
                    "/error",
                    "/favicon.ico",
                    "/**/*.png",
                    "/**/*.gif",
                    "/**/*.svg",
                    "/**/*.jpg",
                    "/**/*.html",
                    "/**/*.css",
                    "/**/*.js")
                    .permitAll()
                .antMatchers("/auth/**", "/oauth2/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                .and()
            .oauth2Login()
                .authorizationEndpoint()
                    .baseUri("/oauth2/authorize")
                    .authorizationRequestRepository(cookieAuthReqRepo)
                    .and()
                .redirectionEndpoint()
                    .baseUri("/oauth2/callback/*")
                    .and()
                .userInfoEndpoint()
                    .userService(oauth2UserService)
                    .and()
                .successHandler(successHandler)
                .failureHandler(failureHandler)

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}
