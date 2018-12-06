package com.rescuehub.rescuehubserver.config

import org.springframework.boot.context.properties.ConfigurationProperties

// These properties have to be declared as var rather than val because Spring gets
// upset if it can't find a setter for them
@ConfigurationProperties(prefix = "app")
data class AppProperties(var auth: Auth = Auth(), var oauth2: OAuth2 = OAuth2())

data class Auth(var jwt: Jwt = Jwt())

data class Jwt(var secret: String = "", var expirationMillis: Long = -1L)

data class OAuth2(var authorizedRedirectUris: List<String> = ArrayList<String>())
