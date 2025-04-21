package io.dnajd.mainservice.service.project_table

import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList

interface ProjectTableService {
    fun findAll(includeIssues: Boolean = false): List<ProjectTable>

    fun getAllByProjectId(projectId: Long, includeIssues: Boolean = false): ProjectTableDtoList

    fun find(id: Long, includeIssues: Boolean = false): ProjectTable

    fun get(id: Long, includeIssues: Boolean = false): ProjectTableDto

    fun create(projectId: Long, dto: ProjectTableDto): ProjectTableDto

    fun update(id: Long, dto: ProjectTableDto): ProjectTableDto

    fun swapTablePositions(fId: Long, sId: Long)

    fun delete(id: Long)
}
