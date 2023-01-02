package io.dnajd.projectservice.web

import io.dnajd.projectservice.model.Project
import io.dnajd.projectservice.model.ProjectRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RequestMapping("/project")
@RestController
class ProjectResource(val projectRepository: ProjectRepository) {
    @GetMapping
    fun getAll(): ProjectHolder {
        return ProjectHolder(projectRepository.findAll())
    }

    @GetMapping("/user/{username}")
    fun getAllForOwner(
        @RequestParam("username") username: String,
    ): ProjectHolder {
        return ProjectHolder(projectRepository.findAllByOwner(username))
    }

    /*
    @GetMapping("/user")
    fun getTest(): String {
        return "Test"
    }
     */

    @PostMapping
    fun post(
        @RequestBody project: Project,
    ): Project {
        return projectRepository.save(project)
    }

    @PutMapping("/{id}")
    fun update(
        @RequestBody project: Project,
        @RequestParam("id") projectId: Long,
    ): Project {
        projectRepository.findById(projectId).orElseThrow { throw IllegalArgumentException("Project with id $projectId does not exist") }
        return projectRepository.save(project)
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun delete(
        @RequestParam("id") projectId: Long
    ) {
        projectRepository.deleteById(projectId)
    }

}
