package com.rescuehub.rescuehubserver.entities

import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "things")
class Thing(
    @Id
    @GeneratedValue
    override val id: Long = 0L,
    val description: String
) : AbstractJpaPersistable<Long>()
