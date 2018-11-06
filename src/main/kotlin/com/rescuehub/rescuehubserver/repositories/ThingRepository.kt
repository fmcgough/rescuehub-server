package com.rescuehub.rescuehubserver.repositories

import org.springframework.data.jpa.repository.JpaRepository
import com.rescuehub.rescuehubserver.entities.Thing

interface ThingRepository : JpaRepository<Thing, Long> {

    fun findByDescription(description: String): Thing?
}
