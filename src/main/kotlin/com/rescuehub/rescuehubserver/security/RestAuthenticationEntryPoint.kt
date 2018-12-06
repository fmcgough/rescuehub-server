package com.rescuehub.rescuehubserver.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint

import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

class RestAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val logger: Logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint::class.java)

    override fun commence(httpServletRequest: HttpServletRequest,
                          httpServletResponse: HttpServletResponse,
                          e: AuthenticationException): Unit {
        logger.error("Responding with unauthorized error. Message - ${e.message}", e)
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                e.getLocalizedMessage())
    }
}
