package com.rescuehub.rescuehubserver.security.oauth2.user

import com.rescuehub.rescuehubserver.model.AuthProvider

abstract class OAuth2UserInfo(protected open val attributes: Map<String, Any>) {

    abstract fun getId(): String

    abstract fun getName(): String

    abstract fun getEmail(): String

    abstract fun getImageUrl(): String

    companion object {

        fun getOAuth2UserInfo(registrationId: String, attributes: Map<String, Any>): OAuth2UserInfo {
            when(registrationId.toUpperCase()) {
                AuthProvider.FACEBOOK.toString() ->
                    return FacebookOAuth2UserInfo(attributes)
                else ->
                    throw IllegalArgumentException("Login with $registrationId not supported")
            }
        }
    }
}
