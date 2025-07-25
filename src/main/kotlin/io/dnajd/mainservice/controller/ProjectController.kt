package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project.ProjectDto
import io.dnajd.mainservice.domain.project.ProjectDtoList
import io.dnajd.mainservice.infrastructure.*
import io.dnajd.mainservice.service.project.ProjectService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT)
class ProjectController(
    private val service: ProjectService,
) {

    @GetMapping("/allByUsername")
    fun getAllByUsername(
        @AuthenticationPrincipal userDetails: UserDetails
    ): ProjectDtoList {
        return service.getAllByUsername(userDetails.username)
    }

    @GetMapping("/{id}")
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Project, PreAuthorizePermission.View)
    fun get(@PathVariable id: Long): ProjectDto {
        return service.get(id)
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody projectDto: ProjectDto
    ): ProjectDto {
        return service.create(userDetails.username, projectDto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Project, PreAuthorizePermission.Owner)
    fun update(
        @PathVariable id: Long,
        @RequestBody projectDto: ProjectDto
    ): ProjectDto {
        return service.update(id, projectDto)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Project, PreAuthorizePermission.Owner)
    fun delete(@PathVariable id: Long) {
        service.delete(id)
    }
}