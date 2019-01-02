package com.rescuehub.rescuehubserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.boot.context.properties.EnableConfigurationProperties
import com.rescuehub.rescuehubserver.config.AppProperties

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties(AppProperties::class)
class RescuehubServerApplication {
}

fun main(args: Array<String>) {
    runApplication<RescuehubServerApplication>(*args)
}
