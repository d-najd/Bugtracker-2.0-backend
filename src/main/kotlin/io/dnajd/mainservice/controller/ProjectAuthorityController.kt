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

    /*
    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/username/{username}/projectId/{projectId}/view/{value}")
    fun modifyViewUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(username, projectId, value, PreAuthorizePermission.View)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/username/{username}/projectId/{projectId}/view/{value}")
    fun modifyCreateUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, value, PreAuthorizePermission.Create)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/username/{username}/projectId/{projectId}/view/{value}")
    fun modifyEditUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, value, PreAuthorizePermission.Edit)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Manage)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/username/{username}/projectId/{projectId}/view/{value}")
    fun modifyDeleteUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, value, PreAuthorizePermission.Delete)
    }

    @CustomPreAuthorize("#projectId", PreAuthorizeType.Project, PreAuthorizePermission.Owner)
    @ResponseStatus(value = HttpStatus.OK)
    @PutMapping("/username/{username}/projectId/{projectId}/view/{value}")
    fun modifyManageUserAuthority(
        @AuthenticationPrincipal userDetails: UserDetails,
        @PathVariable username: String,
        @PathVariable projectId: Long,
        @PathVariable value: Boolean
    ) {
        service.modifyPermission(userDetails.username, projectId, value, PreAuthorizePermission.Manage)
    }
     */
}