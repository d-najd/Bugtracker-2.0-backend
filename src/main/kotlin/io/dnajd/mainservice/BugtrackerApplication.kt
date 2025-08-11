package io.dnajd.mainservice

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean
import io.dnajd.mainservice.infrastructure.user_content.UserContentDirs
import io.dnajd.mainservice.infrastructure.user_content.UserContentPathMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.reactive.function.client.WebClient
import java.nio.file.FileVisitOption
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean::class)
class BugtrackerApplication

@Bean
fun getWebClientBuilder(): WebClient.Builder =
    WebClient.builder()

fun main(args: Array<String>) {
    val defaultProjectIconsPath = Path("${UserContentDirs.ABSOLUTE_PATH}${UserContentDirs.DEFAULT_PROJECT_ICON}")
    val defaultProjectIcons = Files.list(defaultProjectIconsPath).toList()
    val randomProjectIcon = defaultProjectIcons.random()
    val path = randomProjectIcon.absolutePathString()
    val se = UserContentPathMapper.absolutePathToUserContentUri(path)
    defaultProjectIcons.toString()

    // JwtUtil.generateDevToken()
    runApplication<BugtrackerApplication>(*args)
}
