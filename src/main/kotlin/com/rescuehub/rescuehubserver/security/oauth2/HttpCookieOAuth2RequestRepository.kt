package com.rescuehub.rescuehubserver.security.oauth2

import com.rescuehub.rescuehubserver.security.CookieOps
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOAuth2AuthorizationRequestRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest>, CookieOps {

    val oauth2AuthRequestCookieName: String = "oauth2_auth_request"
    val redirectUriCookieName: String = "redirect_uri"
    val cookieExpireSeconds: Int = 180

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        return deserializeCookie<OAuth2AuthorizationRequest>(request, "oauth2_auth_request") ?:
            throw RuntimeException("Could not deserialise OAuth2 authorization request from cookie")
    }

    override fun saveAuthorizationRequest(authorizationRequest: OAuth2AuthorizationRequest?,
                                          request: HttpServletRequest,
                                          response: HttpServletResponse): Unit {
        if (authorizationRequest == null) {
            deleteCookie(request, response, "oauth2_auth_request")
            deleteCookie(request, response, "redirect_uri")
            return
        }

        addCookie(response, "oauth2_auth_request", serialize(authorizationRequest), cookieExpireSeconds)

        val redirectUriAfterLogin = request.getParameter("redirect_uri")

        if (redirectUriAfterLogin != "") {
            addCookie(response, "redirect_uri", redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        return this.loadAuthorizationRequest(request)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest, response: HttpServletResponse): Unit {
        deleteCookie(request, response, "oauth2_auth_request");
        deleteCookie(request, response, "redirect_uri");
    }

}
