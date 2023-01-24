package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.*
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
            .uri("localhost:8095/api/username/$username/includeOwners") // TODO replace the uri with uri from configuration server, alternatively api could be used but that adds extra dependency
            .retrieve()
            .bodyToMono(UserAuthorityHolder::class.java)
            .block()
        val userAuthoritiesFiltered = userAuthorities!!.data
            .filter { it.authority == UserAuthorityType.project_owner
                    || it.authority == UserAuthorityType.project_view }
        val projects = repository.findByIdIn(userAuthoritiesFiltered.map { it.projectId } )
        projects.forEach { project -> project.owner = userAuthoritiesFiltered
            .find { it.projectId == project.id && it.authority == UserAuthorityType.project_owner }!!.username }
        return ProjectHolder(projects)
    }

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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @OptIn(ExperimentalStdlibApi::class)
    @PatchMapping("/{id}/title/{newTitle}")
    fun renameProject(
        @PathVariable("id") id: Long,
        @PathVariable("newTitle") newTitle: String,
    ) {
        repository.findById(id).getOrNull()?.let {
            it.title = newTitle
            repository.saveAndFlush(it)
            return
        }
        throw IllegalArgumentException()
    }

    @PutMapping
    fun update(
        @RequestBody pojo: Project,
    ): Project {
        val originalPojo = repository.findById(pojo.id).orElseThrow { throw IllegalArgumentException("Project with id ${pojo.id} does not exist") }
        return repository.saveAndFlush(originalPojo.copy(
            createdAt = originalPojo.createdAt,
        ))
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }
}
