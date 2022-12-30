package io.dnajd.userprojectservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserProjectServiceApplication

fun main(args: Array<String>) {
	runApplication<UserProjectServiceApplication>(*args)
}
