package io.dnajd.mainservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
class MainServiceApplication

@Bean
fun getWebClientBuilder(): WebClient.Builder =
	WebClient.builder()

fun main(args: Array<String>) {
	runApplication<MainServiceApplication>(*args)
}
