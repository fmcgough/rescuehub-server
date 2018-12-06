package com.rescuehub.rescuehubserver.security

import com.rescuehub.rescuehubserver.entities.User
import com.rescuehub.rescuehubserver.repositories.UserRepository
import com.rescuehub.rescuehubserver.exceptions.ResourceNotFoundException

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomUserDetailsService @Autowired constructor(val userRepo: UserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(email: String): UserDetails {
        val details: UserDetails? = userRepo.findByEmail(email)?.let {
            UserPrincipal.create(it)
        }
        return details ?: throw UsernameNotFoundException("User not found with email : " + email)
    }

    @Transactional
    fun loadUserById(id: Long): UserDetails {
        val details: UserDetails? = userRepo.findById(id).orElse(null)?.let {
            UserPrincipal.create(it)
        }
        return details ?: throw ResourceNotFoundException("User", "id", id)
    }
}
