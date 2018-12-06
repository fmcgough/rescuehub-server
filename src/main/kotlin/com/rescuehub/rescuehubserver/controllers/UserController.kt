package com.rescuehub.rescuehubserver.entities

import com.rescuehub.rescuehubserver.entities.User
import com.rescuehub.rescuehubserver.repositories.UserRepository
import com.rescuehub.rescuehubserver.security.UserPrincipal
import com.rescuehub.rescuehubserver.security.CurrentUser
import com.rescuehub.rescuehubserver.exceptions.ResourceNotFoundException
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.security.access.prepost.PreAuthorize
import javax.inject.Inject

@RestController
@RequestMapping("/user")
class UserController @Inject constructor (val userRepo: UserRepository) {

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    fun getCurrentUser(@CurrentUser userPrincipal: UserPrincipal): User {
        return userRepo.findById(userPrincipal.id).orElse(null) ?:
            throw ResourceNotFoundException("User", "id", userPrincipal.id)
    }

}
