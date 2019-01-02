package com.rescuehub.rescuehubserver.security

import org.springframework.social.connect.web.SignInAdapter
import org.springframework.social.connect.Connection
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * This adapter is a bridge between our application and the ProviderSignInController we
 * configured in our SecurityConfig class. Users logged in with facebook will have role
 * FACEBOOK_USER.
 */
class FacebookSignInAdapter : SignInAdapter {

    override fun signIn(localUserId: String, connection: Connection<*>, request: NativeWebRequest): String? {
        SecurityContextHolder.getContext().setAuthentication(
            UsernamePasswordAuthenticationToken(
                connection.getDisplayName(),
                "",
                listOf(SimpleGrantedAuthority("FACEBOOK_USER"))
            )
        )
        // Return null to indicate the ProviderSignInController should redirect to its postSignInUrl.
        return null
    }
}
