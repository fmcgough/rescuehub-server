package com.rescuehub.rescuehubserver.security

import com.rescuehub.rescuehubserver.config.AppProperties

import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

import java.util.Date

/**
 * Generates and verifies Json Web Tokens.
 */
@Service
class TokenProvider(val appProperties: AppProperties) {

    val logger = LoggerFactory.getLogger(TokenProvider::class.java)

    fun createToken(authentication: Authentication): String {
        val userPrincipal = authentication.getPrincipal() as UserPrincipal

        val now = Date()
        val expiryDate = Date(now.getTime() + appProperties.auth.jwt.expirationMillis)

        return Jwts.builder()
                .setSubject(userPrincipal.id.toString())
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, appProperties.auth.jwt.secret)
                .compact()
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = Jwts.parser()
                .setSigningKey(appProperties.auth.jwt.secret)
                .parseClaimsJws(token)
                .getBody()

        return claims.getSubject().toLong()
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parser().setSigningKey(appProperties.auth.jwt.secret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature", ex)
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token", ex)
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token", ex)
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token", ex)
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.", ex)
        }
        return false
    }
}
