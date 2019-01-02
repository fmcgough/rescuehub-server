package com.rescuehub.rescuehubserver.security

import com.rescuehub.rescuehubserver.entities.User
import com.rescuehub.rescuehubserver.model.AuthProvider
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserPrincipal(
        val id: Long,
        private val email: String,
        private val password: String,
        private val authorities: Collection<GrantedAuthority>) : UserDetails {

    companion object {
        fun create(user: User): UserPrincipal {
            val role = when(user.provider) {
                AuthProvider.FACEBOOK -> "FACEBOOK_USER"
                AuthProvider.LOCAL -> "LOCAL_USER"
            }
            return UserPrincipal(user.id, user.email, user.password,
                listOf(SimpleGrantedAuthority(role)))
        }
    }


    override fun getPassword(): String = password

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

}
