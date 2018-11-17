package com.rescuehub.rescuehubserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.core.Ordered
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.web.filter.ForwardedHeaderFilter

@SpringBootApplication
@EnableJpaRepositories
class RescuehubServerApplication {

}

fun main(args: Array<String>) {
    runApplication<RescuehubServerApplication>(*args)
}
