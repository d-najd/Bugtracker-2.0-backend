package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project_authority.ProjectAuthorityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Endpoints.PROJECT_AUTHORITY)
class ProjectAuthorityController(
    private val projectAuthorityService: ProjectAuthorityService,
) {
    @GetMapping("/testing/findAll")
    fun findAll(): List<ProjectAuthority> {
        return projectAuthorityService.findAll()
    }
}