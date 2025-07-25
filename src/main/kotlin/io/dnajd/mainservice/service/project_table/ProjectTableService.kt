package io.dnajd.mainservice.service.project_table

import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList

interface ProjectTableService {
    fun getAllByProjectId(projectId: Long, includeIssues: Boolean = false): ProjectTableDtoList

    fun get(id: Long, includeIssues: Boolean = false): ProjectTableDto

    fun create(projectId: Long, dto: ProjectTableDto): ProjectTableDto

    fun update(id: Long, dto: ProjectTableDto): ProjectTableDto

    fun swapTablePositions(fId: Long, sId: Long): ProjectTableDtoList

    fun delete(id: Long)
}
