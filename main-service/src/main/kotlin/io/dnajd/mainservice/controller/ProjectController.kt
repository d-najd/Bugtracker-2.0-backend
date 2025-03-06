package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.Project.ProjectList
import io.dnajd.mainservice.domain.Project.ProjectListResponse
import io.dnajd.mainservice.domain.Project.ProjectRequest
import io.dnajd.mainservice.domain.Project.ProjectResponse
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT)
class ProjectController {
    @Autowired
    private lateinit var projectService: ProjectService

    @GetMapping("/testing/getAll")
    fun findAll(): ProjectList {
        return projectService.findAll()
    }

    @GetMapping("/user/{username}")
    fun getAllByUsername(@PathVariable(value = "username") username: String): ProjectListResponse {
        return projectService.getAllByUsername(username)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ProjectResponse {
        return projectService.getById(id)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun createProject(@RequestBody projectRequest: ProjectRequest): ProjectResponse {
        return projectService.createProject(projectRequest)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateProject(
        @PathVariable id: Long,
        @RequestBody projectRequest: ProjectRequest
    ): ProjectResponse {
        return projectService.updateProject(projectRequest)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun removeProject(@PathVariable id: Long) {
        projectService.deleteProject(id)
    }
}