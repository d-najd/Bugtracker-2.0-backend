package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.CustomPreAuthorize
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.infrastructure.PreAuthorizeType
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.table_issue.TableIssueService
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT_TABLE_ISSUE)
class TableIssueController(
    private val issueService: TableIssueService
) {
    @GetMapping("/tableId/{tableId}")
    @CustomPreAuthorize("#tableId", PreAuthorizeType.Table, PreAuthorizePermission.View)
    fun getAllByTableId(
        @PathVariable tableId: Long,
        @RequestParam includeChildIssues: Boolean = false,
    ): TableIssueDtoList {
        return issueService.getAllByTableId(tableId, includeChildIssues)
    }

    @GetMapping("/{id}")
    @CustomPreAuthorize("#id", PreAuthorizeType.Issue, PreAuthorizePermission.View)
    fun get(
        @PathVariable id: Long,
        @RequestParam includeChildIssues: Boolean = true,
        @RequestParam includeAssigned: Boolean = true,
        @RequestParam includeComments: Boolean = true,
        @RequestParam includeLabels: Boolean = true,
    ): TableIssueDto {
        return issueService.get(id, includeChildIssues, includeAssigned, includeComments, includeLabels)
    }

    @PostMapping("/tableId/{tableId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @CustomPreAuthorize("#tableId", PreAuthorizeType.Table, PreAuthorizePermission.Create)
    fun create(
        @PathVariable tableId: Long,
        @RequestBody dto: TableIssueDto,
        @AuthenticationPrincipal userDetails: UserDetails,
    ): TableIssueDto {
        return issueService.create(tableId, userDetails.username, dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @CustomPreAuthorize("#id", PreAuthorizeType.Issue, PreAuthorizePermission.Edit)
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: TableIssueDto
    ): TableIssueDto {
        return issueService.update(id, dto)
    }

    @PatchMapping("/{fId}/swapPositionWith/{sId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#fId", PreAuthorizeType.Issue, PreAuthorizePermission.Edit)
    fun swapIssuePositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ) {
        issueService.swapIssuePositions(fId, sId)
    }

    @PatchMapping("/{fId}/movePositionTo/{sId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#fId", PreAuthorizeType.Issue, PreAuthorizePermission.Edit)
    fun movePositionTo(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ) {
        issueService.movePositionTo(fId, sId)
    }

    @PatchMapping("/{id}/moveToTable/{tableId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#tableId", PreAuthorizeType.Table, PreAuthorizePermission.Edit)
    fun moveToTable(
        @PathVariable id: Long,
        @PathVariable tableId: Long
    ): Int {
        return issueService.moveToTable(id, tableId)
    }

    @PatchMapping("/id/{id}/setParentIssue/{parentIssueId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#id", PreAuthorizeType.Issue, PreAuthorizePermission.Edit)
    fun setParentIssue(
        @PathVariable id: Long,
        @PathVariable parentIssueId: Long
    ) {
        issueService.setParentIssue(id, parentIssueId)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#id", PreAuthorizeType.Issue, PreAuthorizePermission.Delete)
    fun delete(
        @PathVariable id: Long,
    ) {
        issueService.delete(id)
    }
}