package com.rescuehub.rescuehubserver.config

import com.rescuehub.rescuehubserver.security.*

import org.springframework.core.env.Environment

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value

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

import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.connect.support.ConnectionFactoryRegistry
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.social.connect.web.ProviderSignInController

import org.springframework.social.facebook.connect.FacebookConnectionFactory

import javax.inject.Inject

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
class SecurityConfig(
    @Autowired val userDetailsService: CustomUserDetailsService,
    @Autowired val connFactoryLocator: ConnectionFactoryLocator,
    @Autowired val usersConnectionRepo: UsersConnectionRepository
) : WebSecurityConfigurerAdapter() {

    @Value("\${app.base-url}")
    private lateinit var baseUrl: String

    override fun configure(auth: AuthenticationManagerBuilder): Unit {
        auth.userDetailsService(userDetailsService)
    }

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
            .httpBasic()
                .disable()
            .authorizeRequests()
                .antMatchers("/", "/error", "/favicon.ico").permitAll()
                .antMatchers("/login*", "/signin/**")
                    .permitAll()
                .anyRequest()
                    .authenticated()
                    .and()
            .formLogin().loginPage("/login").permitAll()
    }

    @Bean
    fun providerSignInController(): ProviderSignInController {
        val controller = ProviderSignInController(connFactoryLocator, usersConnectionRepo,
            FacebookSignInAdapter())
        controller.setApplicationUrl(baseUrl)
        return controller
    }
}
