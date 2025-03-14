package io.dnajd.mainservice.controller

import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.domain.project_table.ProjectTableList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project_table.ProjectTableService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT_TABLE)
class ProjectTableController(
    private val tableService: ProjectTableService
) {
    @GetMapping("/testing/getAll")
    fun findAll(
        @RequestParam ignoreIssues: Boolean = true,
    ): ProjectTableList {
        return tableService.findAll(ignoreIssues)
    }

    @GetMapping("/projectId/{projectId}")
    fun getAllByProjectId(
        @PathVariable projectId: Long,
        @RequestParam ignoreIssues: Boolean = true,
    ): ProjectTableDtoList {
        return tableService.getAllByProjectId(projectId, ignoreIssues)
    }

    @GetMapping("/id/{id}")
    fun getById(
        @PathVariable id: Long,
        @RequestParam ignoreIssues: Boolean = true,
    ): ProjectTableDto {
        return tableService.getById(id, ignoreIssues)
    }

    @PostMapping("/projectId/{projectId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    fun createTable(@PathVariable projectId: Long, @RequestBody dto: ProjectTableDto): ProjectTableDto {
        return tableService.createTable(projectId, dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    fun updateTable(
        @PathVariable id: Long,
        @RequestBody dto: ProjectTableDto
    ): ProjectTableDto {
        return tableService.updateTable(id, dto)
    }

    @PatchMapping("/{fId}/swapPositionWith/{sId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun swapTablePositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ) {
        return tableService.swapTablePositions(fId, sId)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun deleteTable(@PathVariable id: Long) {
        return tableService.deleteTable(id)
    }
}