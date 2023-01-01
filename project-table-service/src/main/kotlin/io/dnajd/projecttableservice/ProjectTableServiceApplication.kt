package io.dnajd.projecttableservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class ProjectTableServiceApplication

fun main(args: Array<String>) {
	runApplication<ProjectTableServiceApplication>(*args)
}
