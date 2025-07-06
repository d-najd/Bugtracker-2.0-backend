package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.infrastructure.CustomPreAuthorize
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.infrastructure.PreAuthorizeType
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
    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.View)
    fun getByUsernameAndProjectId(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
    ): ProjectAuthorityDtoList {
        return service.findByUsernameAndProjectId(userDetails.username, projectId)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/projectId/{projectId}/view/{value}")
    fun modifyViewUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, PreAuthorizePermission.View, value)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/projectId/{projectId}/create/{value}")
    fun modifyCreateUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, PreAuthorizePermission.Create, value)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/projectId/{projectId}/edit/{value}")
    fun modifyEditUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, PreAuthorizePermission.Edit, value)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/projectId/{projectId}/delete/{value}")
    fun modifyDeleteUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, PreAuthorizePermission.Delete, value)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Owner)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/projectId/{projectId}/manage/{value}")
    fun modifyManageUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, PreAuthorizePermission.Manage, value)
    }
}