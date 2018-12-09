package com.rescuehub.rescuehubserver.repositories

import com.rescuehub.rescuehubserver.entities.User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

}

