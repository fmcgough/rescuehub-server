package com.rescuehub.rescuehubserver

import io.kotlintest.specs.WordSpec
import io.kotlintest.*
import io.kotlintest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.beans.factory.annotation.Autowired
import com.rescuehub.rescuehubserver.controllers.DummyController

@SpringBootTest
class RescuehubServerApplicationTests : WordSpec() {

    @Autowired
    private lateinit var controller: DummyController

    override fun listeners() = listOf(SpringListener)

    init {
        "Application" should {

            "load the context" {
                println("It works") // well it seems to

                controller.repo shouldNotBe null
            }
        }
    }

}
