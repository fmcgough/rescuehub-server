package com.rescuehub.rescuehubserver.security.oauth2

import com.rescuehub.rescuehubserver.repositories.UserRepository
import com.rescuehub.rescuehubserver.exception.OAuth2AuthenticationException
import com.rescuehub.rescuehubserver.entities.User
import com.rescuehub.rescuehubserver.model.AuthProvider
import com.rescuehub.rescuehubserver.security.oauth2.user.OAuth2UserInfo
import com.rescuehub.rescuehubserver.security.UserPrincipal
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.security.core.AuthenticationException
import org.springframework.security.authentication.InternalAuthenticationServiceException
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.util.StringUtils

/**
 * Extends Spring's DefaultOAuth2UserService and implements its loadUser() method.
 * This method is called after an access token is obtained from the OAuth2 provider.
 * In this method, we first fetch the user’s details from the OAuth2 provider. If a
 * user with the same email already exists in our database then we update their details,
 * otherwise, we register a new user.
 */
@Service
class CustomOAuth2UserService @Autowired constructor(val repository: UserRepository) :
    DefaultOAuth2UserService() {

    override fun loadUser(oAuth2UserRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(oAuth2UserRequest)

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User)
        } catch (ex: AuthenticationException) {
            throw ex
        } catch (ex: Exception) {
            // Throwing an instance of AuthenticationException will trigger the
            // OAuth2AuthenticationFailureHandler
            throw InternalAuthenticationServiceException(ex.message, ex.cause)
        }
    }

    private fun processOAuth2User(request: OAuth2UserRequest, oAuth2User: OAuth2User): OAuth2User {
        val userInfo = OAuth2UserInfo.getOAuth2UserInfo(
                request.getClientRegistration().getRegistrationId(),
                oAuth2User.getAttributes())

        if (StringUtils.isEmpty(userInfo.getEmail())) {
            throw OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        val existing: User? = repository.findByEmail(userInfo.getEmail())

        val updated: User? = existing?.let {
            val registeredProvider = AuthProvider.valueOf(request.getClientRegistration().getRegistrationId())

            val provider = it.provider
            if (provider != registeredProvider) {
                throw OAuth2AuthenticationException("Looks like you're signed up with " +
                        "$provider account. Please use your $provider account to login.");
            }
            updateExistingUser(it, userInfo)
        }

        val user = updated ?: registerNewUser(request, userInfo)

        return UserPrincipal.create(user, oAuth2User.getAttributes())
    }

    private fun registerNewUser(request: OAuth2UserRequest, userInfo: OAuth2UserInfo): User {
        val provider = AuthProvider.valueOf(request.getClientRegistration().getRegistrationId())

        val user = User(provider = provider, providerId = userInfo.getId(), name = userInfo.getName(),
            email = userInfo.getEmail(), imageUrl = userInfo.getImageUrl(), password = "")

        return repository.save(user)
    }

    private fun updateExistingUser(existingUser: User, userInfo: OAuth2UserInfo): User {
        val updated = existingUser.copy(name = userInfo.getName(), imageUrl = userInfo.getImageUrl())
        return repository.save(updated)
    }
}
