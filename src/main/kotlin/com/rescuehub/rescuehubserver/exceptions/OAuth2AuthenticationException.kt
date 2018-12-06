package com.rescuehub.rescuehubserver.exception

import org.springframework.security.core.AuthenticationException

data class OAuth2AuthenticationException(override val message: String) : AuthenticationException(message)
