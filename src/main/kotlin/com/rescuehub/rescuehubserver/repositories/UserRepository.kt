package com.rescuehub.rescuehubserver.repositories

import com.naturalprogrammer.spring.lemon.domain.AbstractUserRepository
import com.rescuehub.rescuehubserver.entities.User

interface UserRepository : AbstractUserRepository<User, Long>

