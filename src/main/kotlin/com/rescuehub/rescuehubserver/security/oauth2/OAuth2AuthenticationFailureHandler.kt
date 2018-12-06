package com.rescuehub.rescuehubserver.security.oauth2

import com.rescuehub.rescuehubserver.security.CookieOps

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

@Component
class OAuth2AuthenticationFailureHandler @Autowired constructor(
        val repo: HttpCookieOAuth2AuthorizationRequestRepository
    ) : SimpleUrlAuthenticationFailureHandler(), CookieOps {

    override fun onAuthenticationFailure(request: HttpServletRequest, 
                                         response: HttpServletResponse,
                                         exception: AuthenticationException): Unit {
        val targetUrl: String = deserializeCookie<String>(request, "redirect_uri") ?: "/"

        val urlWithError = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString()

        repo.removeAuthorizationRequestCookies(request, response)

        getRedirectStrategy().sendRedirect(request, response, urlWithError)
    }
}
