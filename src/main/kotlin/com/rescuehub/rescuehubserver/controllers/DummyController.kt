package com.rescuehub.rescuehubserver.controllers

import java.util.concurrent.atomic.AtomicLong
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Message(val id: Long, val message: String)

@RestController
class DummyController {

    val counter = AtomicLong()

    @GetMapping("/hello")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Message =
        Message(counter.incrementAndGet(), "Hello, $name!")
}
