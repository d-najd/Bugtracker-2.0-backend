package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.ScopedAuthorize
import io.dnajd.mainservice.infrastructure.ScopedPermission
import io.dnajd.mainservice.infrastructure.ScopedEvaluatorType
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.infrastructure.Endpoints
import io.dnajd.mainservice.service.project_table.ProjectTableService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(Endpoints.PROJECT_TABLE)
class ProjectTableController(
    private val service: ProjectTableService
) {
    @GetMapping("/projectId/{projectId}")
    @ScopedAuthorize("#projectId", ScopedEvaluatorType.Project, ScopedPermission.View)
    fun getAllByProjectId(
        @PathVariable projectId: Long,
        @RequestParam includeIssues: Boolean = false,
    ): ProjectTableDtoList {
        return service.getAllByProjectId(projectId, includeIssues)
    }

    @GetMapping("/{id}")
    @ScopedAuthorize("#id", ScopedEvaluatorType.Table, ScopedPermission.View)
    fun get(
        @PathVariable id: Long,
        @RequestParam includeIssues: Boolean = false,
    ): ProjectTableDto {
        return service.get(id, includeIssues)
    }

    @PostMapping("/projectId/{projectId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @ScopedAuthorize("#projectId", ScopedEvaluatorType.Project, ScopedPermission.Create)
    fun create(
        @PathVariable projectId: Long,
        @RequestBody dto: ProjectTableDto
    ): ProjectTableDto {
        return service.create(projectId, dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @ScopedAuthorize("#id", ScopedEvaluatorType.Table, ScopedPermission.Edit)
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: ProjectTableDto
    ): ProjectTableDto {
        return service.update(id, dto)
    }

    @PatchMapping("/{fId}/swapPositionWith/{sId}")
    @ScopedAuthorize("#fId", ScopedEvaluatorType.Table, ScopedPermission.Edit)
    fun swapTablePositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ): ProjectTableDtoList {
        return service.swapTablePositions(fId, sId)
    }

    @DeleteMapping("/{id}")
    @ScopedAuthorize("#id", ScopedEvaluatorType.Table, ScopedPermission.Delete)
    fun delete(@PathVariable id: Long): ProjectTableDtoList {
        return service.delete(id)
    }
}