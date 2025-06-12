package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.infrastructure.CustomPreAuthorize
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.infrastructure.PreAuthorizeType
import io.dnajd.mainservice.service.project_authority.ProjectAuthorityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Endpoints.PROJECT_AUTHORITY)
class ProjectAuthorityController(
    private val service: ProjectAuthorityService,
) {
    @GetMapping("/testing/findAll")
    fun findAllTesting(): List<ProjectAuthority> {
        return service.findAllTesting()
    }

    @GetMapping("/username/{username}/projectId/{projectId}")
    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.View)
    fun getByUsernameAndProjectId(
        @PathVariable username: String,
        @PathVariable projectId: Long,
    ): ProjectAuthorityDtoList {
        return service.findByUsernameAndProjectId(username, projectId)
    }
}