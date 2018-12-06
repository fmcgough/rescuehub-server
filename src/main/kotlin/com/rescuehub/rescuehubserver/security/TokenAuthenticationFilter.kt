package com.rescuehub.rescuehubserver.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException

class TokenAuthenticationFilter @Autowired constructor(
        val tokenProvider: TokenProvider,
        val detailsService: CustomUserDetailsService
    ) : OncePerRequestFilter() {

    override protected fun doFilterInternal(request: HttpServletRequest,
                                            response: HttpServletResponse,
                                            filterChain: FilterChain): Unit {
        try {
            val jwt = getJwtFromRequest(request)

            jwt?.let {
                val userId = tokenProvider.getUserIdFromToken(it)

                val userDetails = detailsService.loadUserById(userId)
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities())

                authentication.setDetails(WebAuthenticationDetailsSource().buildDetails(request))

                SecurityContextHolder.getContext().setAuthentication(authentication)
            }
        } catch (ex: Exception) {
            logger.error("Could not set user authentication in security context", ex)
        }

        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.let { it.removePrefix("Bearer ") }
    }
}
