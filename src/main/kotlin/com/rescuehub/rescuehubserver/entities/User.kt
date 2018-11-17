package com.rescuehub.rescuehubserver.entities

import javax.persistence.Entity
import javax.persistence.Table

import com.naturalprogrammer.spring.lemon.domain.AbstractUser

@Entity
@Table(name="users")
class User : AbstractUser<Long>() {

}

