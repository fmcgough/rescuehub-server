package com.rescuehub.rescuehubserver.controllers

import javax.inject.Inject
import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import com.rescuehub.rescuehubserver.repositories.ThingRepository
import com.rescuehub.rescuehubserver.entities.Thing as ThingEntity
import org.springframework.beans.factory.annotation.Autowired

data class Message(val id: Long, val message: String)

data class Thing(val id: Long?, val description: String)

@RestController
class DummyController @Inject constructor(val repo: ThingRepository) {

    val counter = AtomicLong()


    @GetMapping("/hello")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Message =
        Message(counter.incrementAndGet(), "Hello, $name!")

    @GetMapping("/things")
    fun things(): List<Thing> = repo.findAll().map { thing ->
        Thing(thing.id, thing.description)
   }

   @PostMapping("/things/create")
   fun createThing(@RequestBody thing: Thing): Thing {
        val saved = repo.save(ThingEntity(0L, thing.description))
        return Thing(saved.id, thing.description)
   }
}
