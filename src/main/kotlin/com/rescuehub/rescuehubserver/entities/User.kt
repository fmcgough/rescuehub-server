package com.rescuehub.rescuehubserver.entities

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import com.fasterxml.jackson.annotation.JsonIgnore
import com.rescuehub.rescuehubserver.model.AuthProvider

@Entity
@Table(name = "users", uniqueConstraints = [UniqueConstraint(columnNames = ["email"])])
data class User(
    @Id
    @GeneratedValue
    override val id: Long = 0L,

    @Column(nullable = false)
    val name: String,

    @Email
    @Column(nullable = false)
    val email: String,

    @Column
    val imageUrl: String,

    @JsonIgnore
    val password: String,

    @NotNull
    @Enumerated(EnumType.STRING)
    val provider: AuthProvider

) : AbstractJpaPersistable<Long>()

