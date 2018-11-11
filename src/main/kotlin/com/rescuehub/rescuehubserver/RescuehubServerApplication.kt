package com.rescuehub.rescuehubserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
class RescuehubServerApplication {

    /**
     * Configuration for cross-origin HTTP requests, so that the UI can access the API.
     */
    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object: WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry): Unit {
                // TODO get allowed origins from config
                registry.addMapping("/**").allowedOrigins("*")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<RescuehubServerApplication>(*args)
}
