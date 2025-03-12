package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.domain.project.ProjectDtoList
import io.dnajd.mainservice.domain.project.ProjectList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT)
class ProjectController(
    private val projectService: ProjectService,
) {

    @GetMapping("/testing/getAll")
    fun findAll(): ProjectList {
        return projectService.findAll()
    }

    @GetMapping("/user/{username}")
    fun getAllByUsername(@PathVariable username: String): ProjectDtoList {
        return projectService.getAllByUsername(username)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ProjectDto {
        return projectService.getById(id)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createProject(@RequestBody projectDto: ProjectDto): ProjectDto {
        return projectService.createProject(projectDto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateProject(
        @PathVariable id: Long,
        @RequestBody projectDto: ProjectDto
    ): ProjectDto {
        return projectService.updateProject(id, projectDto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun removeProject(@PathVariable id: Long) {
        projectService.deleteProject(id)
    }
}