package com.rescuehub.rescuehubserver.entities

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "things")
class Thing(
    val description: String
) : AbstractJpaPersistable<Long>()
