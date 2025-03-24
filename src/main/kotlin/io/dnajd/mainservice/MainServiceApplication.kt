package io.dnajd.mainservice

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.reactive.function.client.WebClient

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean::class)
class MainServiceApplication

@Bean
fun getWebClientBuilder(): WebClient.Builder =
	WebClient.builder()

fun main(args: Array<String>) {
	runApplication<MainServiceApplication>(*args)
}
