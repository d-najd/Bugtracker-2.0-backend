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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RequestMapping
@RestController
class ProjectResource(val projectRepository: ProjectRepository) {
    @GetMapping("/testing/getAll")
    fun getAll(): ProjectHolder {
        return ProjectHolder(projectRepository.findAll())
    }

    @GetMapping("/user/{username}")
    fun getAllForOwner(
        @PathVariable username: String
    ): ProjectHolder {
        return ProjectHolder(projectRepository.findAllByOwner(username!!))
    }

    @PostMapping("/user/{username}")
    fun post(
        @PathVariable username: String,
        @RequestBody project: Project,
    ): Project {
        project.owner = username
        return projectRepository.save(project)
    }

    @PutMapping("/{id}")
    fun update(
        @RequestBody project: Project,
        @PathVariable id: Long,
    ): Project {
        projectRepository.findById(id).orElseThrow { throw IllegalArgumentException("Project with id $id does not exist") }
        project.id = id
        return projectRepository.save(project)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Long
    ) {
        projectRepository.deleteById(id)
    }
}
