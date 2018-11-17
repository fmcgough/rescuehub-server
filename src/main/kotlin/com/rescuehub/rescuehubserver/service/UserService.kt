package com.rescuehub.rescuehubserver.service

import org.springframework.stereotype.Service
import com.naturalprogrammer.spring.lemon.LemonService
import com.rescuehub.rescuehubserver.entities.User

@Service
class UserService : LemonService<User, Long>() {

    override fun newUser(): User = User()

    override fun toId(id: String): Long = id.toLong()
}
