package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.Project
import io.dnajd.projectservice.model.ProjectRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.IllegalArgumentException
import kotlin.jvm.optionals.getOrNull

@RequestMapping("/api")
@RestController
class ProjectResource(val repository: ProjectRepository) {
    @GetMapping("/testing/getAll")
    fun getAll(): ProjectHolder {
        return ProjectHolder(repository.findAll())
    }

    @GetMapping("/user/{username}")
    fun getAllByOwner(
        @PathVariable username: String
    ): ProjectHolder {
        return ProjectHolder(repository.findAllByOwner(username))
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long
    ): Project {
        return repository.findById(id).getOrNull() ?: throw IllegalArgumentException()
    }

    @PostMapping
    fun post(
        @RequestBody pojo: Project,
    ): Project {
        val transientPojo = pojo.copy(
            createdAt = Date(),
        )
        return repository.save(transientPojo)
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
