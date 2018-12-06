package com.rescuehub.rescuehubserver.security.oauth2

import com.rescuehub.rescuehubserver.config.AppProperties
import com.rescuehub.rescuehubserver.security.CookieOps
import com.rescuehub.rescuehubserver.security.TokenProvider
import com.rescuehub.rescuehubserver.exceptions.BadRequestException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.ServletException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.net.URI

@Component
class OAuth2AuthenticationSuccessHandler @Autowired constructor(
        val tokenProvider: TokenProvider,
        val appProperties: AppProperties,
        val repo: HttpCookieOAuth2AuthorizationRequestRepository
    ) : SimpleUrlAuthenticationSuccessHandler(), CookieOps {

    override fun onAuthenticationSuccess(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         authentication: Authentication): Unit {
        val targetUrl = determineTargetUrl(request, response, authentication)

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl)
        } else {
            clearAuthenticationAttributes(request, response)
            getRedirectStrategy().sendRedirect(request, response, targetUrl)
        }
    }

    protected fun determineTargetUrl(request: HttpServletRequest,
                                     response: HttpServletResponse,
                                     authentication: Authentication): String {
        val redirectUri: String? = deserializeCookie<String>(request, "redirect_uri")

        redirectUri?.let {
            if (!isAuthorizedRedirectUri(it)) {
                throw BadRequestException("Unauthorised redirect_uri ${redirectUri}")
            }
        }

        val targetUrl: String = redirectUri ?: getDefaultTargetUrl()
        val token = tokenProvider.createToken(authentication)

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest,
                                                response: HttpServletResponse): Unit {
        super.clearAuthenticationAttributes(request)
        repo.removeAuthorizationRequestCookies(request, response)
    }

    private fun isAuthorizedRedirectUri(uri: String): Boolean {
        val clientRedirectUri = URI.create(uri)

        return appProperties.oauth2.authorizedRedirectUris.any {
            // Only validate host and port. Let the clients use different paths if they want to
            val authorizedURI = URI.create(it)
            authorizedURI.getHost().toLowerCase() == clientRedirectUri.getHost().toLowerCase()
                    && authorizedURI.getPort() == clientRedirectUri.getPort()
        }
    }
}
