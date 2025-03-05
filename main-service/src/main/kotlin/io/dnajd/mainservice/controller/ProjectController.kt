package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.ProjectList
import io.dnajd.mainservice.dto.ProjectDto
import io.dnajd.mainservice.dto.ProjectListDto
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
    fun getAll(): ProjectList {
        return projectService.findAll()
    }

    @GetMapping("/user/{username}")
    fun getAllByUsername(@PathVariable(value = "username") username: String): ProjectListDto {
        return projectService.getAllByUsername(username)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ProjectDto {
        return projectService.getById(id)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun createProject(@RequestBody projectDto: ProjectDto): ProjectDto {
        return projectService.createProject(projectDto)
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    fun updateProject(@RequestBody projectDto: ProjectDto): ProjectDto {
        return projectService.updateProject(projectDto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun removeProject(@PathVariable id: Long) {
        projectService.deleteProject(id)
    }
}