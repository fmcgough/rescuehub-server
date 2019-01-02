package com.rescuehub.rescuehubserver.config

import com.rescuehub.rescuehubserver.security.FacebookConnectionSignup

import org.springframework.core.env.Environment
import org.springframework.context.annotation.Configuration
import org.springframework.social.security.AuthenticationNameUserIdSource
import org.springframework.social.UserIdSource
import org.springframework.social.connect.ConnectionFactoryLocator
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer
import org.springframework.social.config.annotation.EnableSocial
import org.springframework.social.config.annotation.SocialConfigurer
import org.springframework.social.facebook.connect.FacebookConnectionFactory
import org.springframework.social.connect.UsersConnectionRepository
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository
import org.springframework.beans.factory.annotation.Autowired

@Configuration
@EnableSocial
class SocialConfig(@Autowired val fbSignup: FacebookConnectionSignup) : SocialConfigurer {

    override fun addConnectionFactories(cfConfig: ConnectionFactoryConfigurer,
                                        env: Environment): Unit {
        cfConfig.addConnectionFactory(
            FacebookConnectionFactory(env.getProperty("spring.social.facebook.appId"),
            env.getProperty("spring.social.facebook.appSecret"))
        )
    }


    /**
     * Singleton data access object providing access to connections across all users.
     */
    override fun getUsersConnectionRepository(connectionFactoryLocator: ConnectionFactoryLocator): UsersConnectionRepository {
        val repository = InMemoryUsersConnectionRepository(connectionFactoryLocator)
        repository.setConnectionSignUp(fbSignup)
        return repository
    }

    override fun getUserIdSource(): UserIdSource = AuthenticationNameUserIdSource()
}
