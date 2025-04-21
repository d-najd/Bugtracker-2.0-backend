package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.domain.project.ProjectDtoList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT)
class ProjectController(
    private val service: ProjectService,
) {
    @GetMapping("/testing/findAll")
    fun findAll(): List<Project> {
        return service.findAll()
    }

    @GetMapping("/username/{username}")
    fun getAllByUsername(@PathVariable username: String): ProjectDtoList {
        return service.getAllByUsername(username)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): ProjectDto {
        return service.get(id)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@RequestBody projectDto: ProjectDto): ProjectDto {
        return service.create(projectDto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun update(
        @PathVariable id: Long,
        @RequestBody projectDto: ProjectDto
    ): ProjectDto {
        return service.update(id, projectDto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}