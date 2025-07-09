package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import io.dnajd.mainservice.infrastructure.CustomPreAuthorize
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.PreAuthorizeEvaluator
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.service.project_authority.ProjectAuthorityService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT_AUTHORITY)
class ProjectAuthorityController(
    private val service: ProjectAuthorityService,
) {
    @GetMapping("/projectId/{projectId}")
    @CustomPreAuthorize("#projectId", PreAuthorizeEvaluator.Project, PreAuthorizePermission.View)
    fun getAllByProjectId(
        @PathVariable projectId: Long,
    ): ProjectAuthorityDtoList {
        return service.findAllByProjectId(projectId)
    }

    /**
     * Manager and owner are allowed to call this
     */
    @CustomPreAuthorize(
        "#projectAuthorityId",
        PreAuthorizeEvaluator.HasGrantingAuthority,
        PreAuthorizePermission.Manage
    )
    @PostMapping("/userAuthority/value/{value}")
    @ResponseStatus(value = HttpStatus.OK)
    fun modifyUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody projectAuthorityId: ProjectAuthorityIdentity,
        @PathVariable value: Boolean
    ) {
        if (!(projectAuthorityId.authority == PreAuthorizePermission.View.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Edit.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Delete.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Create.value
                    )
        ) {
            throw IllegalArgumentException("Authority not in legal authority types")
        }

        service.modifyUserAuthority(userDetails, projectAuthorityId, value)
    }

    /**
     * Only owner is able to call this
     */
    @CustomPreAuthorize(
        "#projectAuthorityId",
        PreAuthorizeEvaluator.HasGrantingAuthority,
        PreAuthorizePermission.Owner
    )
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping("/managerAuthority/value/{value}")
    fun modifyManagerAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @RequestBody projectAuthorityId: ProjectAuthorityIdentity,
        @PathVariable value: Boolean
    ) {
        if (!(projectAuthorityId.authority == PreAuthorizePermission.View.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Edit.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Delete.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Create.value ||
                    projectAuthorityId.authority == PreAuthorizePermission.Manage.value
                    )
        ) {
            throw IllegalArgumentException("Authority not in legal authority types")
        }

        service.modifyManagerAuthority(userDetails, projectAuthorityId, value)
    }
}