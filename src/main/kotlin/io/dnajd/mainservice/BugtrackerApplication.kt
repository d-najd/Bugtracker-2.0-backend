package io.dnajd.mainservice

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean
import io.dnajd.mainservice.domain.project.Project
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.reactive.function.client.WebClient
import kotlin.reflect.jvm.jvmName

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean::class)
class BugtrackerApplication

@Bean
fun getWebClientBuilder(): WebClient.Builder =
    WebClient.builder()

fun main(args: Array<String>) {
    runApplication<BugtrackerApplication>(*args)
}
