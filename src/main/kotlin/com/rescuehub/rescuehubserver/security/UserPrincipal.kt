package com.rescuehub.rescuehubserver.security

import com.rescuehub.rescuehubserver.entities.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

data class UserPrincipal(
        val id: Long,
        private val email: String,
        private val password: String,
        private val authorities: Collection<GrantedAuthority>,
        private val attributes: Map<String, Any> = emptyMap<String, Any>()) : OAuth2User, UserDetails {
         
    companion object {
        fun create(user: User): UserPrincipal {
            return UserPrincipal(user.id, user.email, user.password,
                listOf(SimpleGrantedAuthority("ROLE_USER")))
        }

        fun create(user: User, attributes: Map<String, Any>): UserPrincipal {
            return create(user).copy(attributes = attributes)
        }
    }


    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getName(): String = id.toString()
}
