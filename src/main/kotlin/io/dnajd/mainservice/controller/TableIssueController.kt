package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.table_issue.TableIssueService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT_TABLE_ISSUE)
class TableIssueController(
    private val issueService: TableIssueService
) {
    @GetMapping("/testing/findAll")
    fun findAll(): List<TableIssue> {
        return issueService.findAll()
    }

    @GetMapping("/tableId/{tableId}")
    fun getAllByTableId(
        @PathVariable tableId: Long,
        @RequestParam includeChildIssues: Boolean = false,
    ): TableIssueDtoList {
        return issueService.getAllByTableId(tableId, includeChildIssues)
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long,
        @RequestParam includeChildIssues: Boolean = true,
        @RequestParam includeAssigned: Boolean = true,
        @RequestParam includeComments: Boolean = true,
        @RequestParam includeLabels: Boolean = true,
    ): TableIssueDto {
        return issueService.getById(id, includeChildIssues, includeAssigned, includeComments, includeLabels)
    }

    @PostMapping("/tableId/{tableId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createIssue(
        @PathVariable tableId: Long,
        @RequestBody dto: TableIssueDto
    ): TableIssueDto {
        return issueService.createIssue(tableId, "user1", dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateIssue(
        @PathVariable id: Long,
        @RequestBody dto: TableIssueDto
    ): TableIssueDto {
        return issueService.updateIssue(id, dto)
    }

    @PatchMapping("/{fId}/swapPositionWith/{sId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun swapIssuePositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ) {
        issueService.swapIssuePositions(fId, sId)
    }

    @PatchMapping("/{fId}/movePositionTo/{sId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun moveTaskPositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ) {
        issueService.movePositionTo(fId, sId)
    }

    @PatchMapping("/{id}/moveToTable/{tableId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun changeToTable(
        @PathVariable id: Long,
        @PathVariable tableId: Long
    ): Int {
        return issueService.changeTable(id, tableId)
    }

    @PatchMapping("{id}/setParentIssue/{parentIssueId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun setParentIssue(
        @PathVariable id: Long,
        @PathVariable parentIssueId: Long
    ) {
        issueService.setParentIssue(id, parentIssueId)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteById(
        @PathVariable id: Long,
    ) {
        issueService.deleteById(id)
    }
}