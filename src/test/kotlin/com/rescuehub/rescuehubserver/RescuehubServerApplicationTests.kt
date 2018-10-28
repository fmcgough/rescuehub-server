package com.rescuehub.rescuehubserver

import io.kotlintest.specs.WordSpec
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class RescuehubServerApplicationTests : WordSpec() {

    override fun listeners() = listOf(SpringListener)

    init {
        "Application" should {

            "load the context" {
                println("It works") // well it seems to
            }
        }
    }

}
