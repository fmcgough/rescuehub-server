package com.rescuehub.rescuehubserver.security.oauth2.user

class FacebookOAuth2UserInfo(override val attributes: Map<String, Any>) : OAuth2UserInfo(attributes) {

    override fun getId(): String = (attributes.getOrDefault("id", 0) as Int).toString()

    override fun getName(): String = attributes.getOrDefault("name", "") as String

    override fun getEmail(): String = attributes.getOrDefault("email", "") as String

    override fun getImageUrl(): String {
        return ((attributes.getOrDefault("picture", emptyMap<String, Any>()) as Map<String, Any>).
            getOrDefault("data", emptyMap<String, Any>()) as Map<String, Any>).
            getOrDefault("url", "") as String
    }
}
