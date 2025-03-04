package io.dnajd.mainservice.web

import io.dnajd.mainservice.model.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import java.util.*
import kotlin.IllegalArgumentException
import kotlin.jvm.optionals.getOrNull

@RequestMapping("/api")
@RestController
class ProjectResource(val repository: ProjectRepository) {
    @Autowired
    private lateinit var webClientBuilder: WebClient.Builder

    // unstable
    @GetMapping("/testing/getAll")
    fun getAll(): ProjectHolder {
        return ProjectHolder(repository.findAll())
    }

    @GetMapping("/user/{username}")
    fun getAllByUser(
        @PathVariable username: String
    ): ProjectHolder {
        val userAuthorities = webClientBuilder.build()
            .get()
            // TODO replace the uri with uri from configuration server, alternatively api could be used but that adds extra dependency
            .uri { builder ->
                builder
                    .path("localhost:8095/api/username/$username")
                    .queryParam("includeOwners", true)
                    .build()
            }
            .retrieve()
            .bodyToMono(UserAuthorityHolder::class.java)
            .block()
        val userAuthoritiesFiltered = userAuthorities!!.data
            .filter {
                it.authority == UserAuthorityType.project_owner
                        || it.authority == UserAuthorityType.project_view
            }
        val projects = repository.findByIdIn(userAuthoritiesFiltered.map { it.projectId })
        projects.mapOwners(owners = userAuthoritiesFiltered)
        return ProjectHolder(projects)
    }

    // unstable
    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): Project {
        return repository.findById(id).getOrNull() ?: throw IllegalArgumentException()
    }

    // TODO communicate with user authority service to make sure that owner is set for the project
    @PostMapping
    fun post(
        @RequestBody pojo: Project,
    ): Project {
        val transientPojo = pojo.copy(
            createdAt = Date(),
        )
        val persistedPojo = repository.save(transientPojo)

        val transientUserAuthority = UserAuthority(
            username = "user1", // TODO this is hard coded
            projectId = persistedPojo.id,
            authority = UserAuthorityType.project_owner,
        )
        webClientBuilder.build()
            .post()
            .uri("localhost:8095/api") // TODO replace the uri with uri from configuration server, alternatively api could be used but that adds extra dependency
            .bodyValue(transientUserAuthority)
            .retrieve()
            .bodyToMono(UserAuthority::class.java)
            .doOnError {
                repository.deleteById(persistedPojo.id)
            }
            .block()
        return persistedPojo
    }

    @PutMapping
    fun update(
        @PathVariable("id") id: Long,
        @RequestParam("title") title: String? = null,
        @RequestParam("description") description: String? = null,
        @RequestParam("returnBody") shouldReturnBody: Boolean = true,
    ): Project? {
        val persistedProject = repository.findById(id).orElseThrow { throw IllegalArgumentException("Project with id $id does not exist") }
        val returnBody = repository.saveAndFlush(
            persistedProject.copy(
                title = title ?: persistedProject.title,
                description = description ?: persistedProject.description,
            )
        )
        return if(shouldReturnBody) returnBody else null
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }
}
