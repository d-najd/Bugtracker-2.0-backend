package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.Project
import io.dnajd.projectservice.model.ProjectRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException
import java.util.*

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

    @PostMapping
    fun post(
        @RequestBody pojo: Project,
    ): Project {
        val transientPojo = pojo.copy(
            createdAt = Date(),
        )
        return repository.save(transientPojo)
    }

    @PutMapping
    fun update(
        @RequestBody pojo: Project,
    ): Project {
        repository.findById(pojo.id).orElseThrow { throw IllegalArgumentException("Project with id ${pojo.id} does not exist") }
        return repository.saveAndFlush(pojo)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        repository.deleteById(id)
    }
}
