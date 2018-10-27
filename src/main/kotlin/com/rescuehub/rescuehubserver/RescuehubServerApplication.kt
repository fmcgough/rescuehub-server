package com.rescuehub.rescuehubserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RescuehubServerApplication

fun main(args: Array<String>) {
    runApplication<RescuehubServerApplication>(*args)
}
