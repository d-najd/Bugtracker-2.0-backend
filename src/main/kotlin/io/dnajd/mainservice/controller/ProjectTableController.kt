package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.CustomPreAuthorize
import io.dnajd.mainservice.infrastructure.PreAuthorizePermission
import io.dnajd.mainservice.infrastructure.PreAuthorizeEvaluator
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
    @CustomPreAuthorize("#projectId", PreAuthorizeEvaluator.Project, PreAuthorizePermission.View)
    fun getAllByProjectId(
        @PathVariable projectId: Long,
        @RequestParam includeIssues: Boolean = false,
    ): ProjectTableDtoList {
        return service.getAllByProjectId(projectId, includeIssues)
    }

    @GetMapping("/{id}")
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Table, PreAuthorizePermission.View)
    fun get(
        @PathVariable id: Long,
        @RequestParam includeIssues: Boolean = false,
    ): ProjectTableDto {
        return service.get(id, includeIssues)
    }

    @PostMapping("/projectId/{projectId}")
    @ResponseStatus(value = HttpStatus.CREATED)
    @CustomPreAuthorize("#projectId", PreAuthorizeEvaluator.Project, PreAuthorizePermission.Create)
    fun create(
        @PathVariable projectId: Long,
        @RequestBody dto: ProjectTableDto
    ): ProjectTableDto {
        return service.create(projectId, dto)
    }

    @PutMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Table, PreAuthorizePermission.Edit)
    fun update(
        @PathVariable id: Long,
        @RequestBody dto: ProjectTableDto
    ): ProjectTableDto {
        return service.update(id, dto)
    }

    /**
     * @return returns the swapped tables
     */
    @PatchMapping("/{fId}/swapPositionWith/{sId}")
    @CustomPreAuthorize("#fId", PreAuthorizeEvaluator.Table, PreAuthorizePermission.Edit)
    fun swapTablePositions(
        @PathVariable fId: Long,
        @PathVariable sId: Long
    ): ProjectTableDtoList {
        return service.swapTablePositions(fId, sId)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CustomPreAuthorize("#id", PreAuthorizeEvaluator.Table, PreAuthorizePermission.Delete)
    fun delete(@PathVariable id: Long) {
        return service.delete(id)
    }
}