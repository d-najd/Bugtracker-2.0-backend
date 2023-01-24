package io.dnajd.projectservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class ProjectServiceApplication

@Bean
fun getWebClientBuilder(): WebClient.Builder =
	WebClient.builder()

fun main(args: Array<String>) {
	runApplication<ProjectServiceApplication>(*args)
}
