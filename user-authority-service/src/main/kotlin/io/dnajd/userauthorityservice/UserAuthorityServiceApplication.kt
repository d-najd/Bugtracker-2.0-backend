package io.dnajd.userauthorityservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserAuthorityServiceApplication

fun main(args: Array<String>) {
	runApplication<UserAuthorityServiceApplication>(*args)
}
