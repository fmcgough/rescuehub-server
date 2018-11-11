package com.rescuehub.rescuehubserver.controllers

import com.rescuehub.rescuehubserver.repositories.ThingRepository
import com.rescuehub.rescuehubserver.entities.Thing as ThingEntity
import io.kotlintest.specs.WordSpec
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.context.web.WebAppConfiguration
import com.nhaarman.mockitokotlin2.*

@WebAppConfiguration
class DummyControllerSpec : WordSpec() {

    override fun listeners() = listOf(SpringListener)

    private lateinit var repo: ThingRepository

    private var mockMvc: MockMvc? = null

    override fun beforeSpec(description: Description, spec: Spec) {
        repo = mock()
        mockMvc = standaloneSetup(DummyController(repo)).build()
    }

    init {
        "DummyController" should {

            "respond to get requests" {
                mockMvc?.perform(get("/hello").param("name", "Frankie"))?.
                    andDo(print())?.
                    andExpect(status().isOk())?.
                    andExpect(jsonPath("message").value("Hello, Frankie!"))?.
                    andExpect(jsonPath("id").isNumber())
            }

            "respond to get requests to /things" {
                whenever(repo.findAll()).thenReturn(
                    listOf(ThingEntity(id = 1L, description = "thing one"),
                       ThingEntity(id = 123L, description = "thing two"))
                )
                mockMvc?.perform(get("/things"))?.andDo(print())?.
                    andExpect(status().isOk())?.
                    andExpect(content().json(
                            """[{"id":1,"description":"thing one"},{"id":123,"description":"thing two"}]"""
                    ))

                verify(repo).findAll()
            }

            "save a new thing on post to /things/create" {
                whenever(repo.save(any<ThingEntity>())).
                    thenReturn(ThingEntity(id = 123L, description = "a thing"))

                mockMvc?.perform(post("/things/create").
                        contentType("application/json").
                        content("""{"description":"a thing"}"""))?.
                    andDo(print())?.
                    andExpect(status().isOk())?.
                    andExpect(content().json("{'id':123,'description':'a thing'}"))

                verify(repo).save(any<ThingEntity>())
            }
        }
    }
}
