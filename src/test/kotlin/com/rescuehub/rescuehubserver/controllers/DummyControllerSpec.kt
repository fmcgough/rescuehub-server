package com.rescuehub.rescuehubserver.controllers

import io.kotlintest.specs.WordSpec
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.spring.SpringListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*
import org.springframework.test.context.web.WebAppConfiguration

@WebAppConfiguration
class DummyControllerSpec : WordSpec() {

    override fun listeners() = listOf(SpringListener)

    private var mockMvc: MockMvc? = null

    override fun beforeSpec(description: Description, spec: Spec) {
        mockMvc = standaloneSetup(DummyController()).build()
    }

    init {
        "DummyController" should {

            "do the thing" {
                mockMvc?.perform(get("/hello").param("name", "Frankie"))?.
                    andDo(print())?.
                    andExpect(status().isOk())?.
                    andExpect(jsonPath("message").value("Hello, Frankie!"))?.
                    andExpect(jsonPath("id").isNumber())
            }
        }
    }
}
