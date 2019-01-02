package com.rescuehub.rescuehubserver.security

import com.rescuehub.rescuehubserver.repositories.UserRepository
import com.rescuehub.rescuehubserver.entities.User
import com.rescuehub.rescuehubserver.model.AuthProvider
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.social.connect.Connection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory

/**
 * When a user authenticates with Facebook for the first time, they have no existing
 * account in our application. This service will ensure that an account is created automatically
 * for them.
 */
@Service
class FacebookConnectionSignup(@Autowired val userRepo: UserRepository) : ConnectionSignUp {

    private val log = LoggerFactory.getLogger(javaClass.getName())

    override fun execute(connection: Connection<*>): String {
        val user = User(
            name = connection.getDisplayName(),
            email = connection.fetchUserProfile().getEmail(),
            imageUrl = connection.getImageUrl(),
            password = "",
            provider = AuthProvider.FACEBOOK
        )
        val saved = userRepo.save(user)
        log.info("User {} signed in", connection.getDisplayName())
        return saved.name
    }
}
