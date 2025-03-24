package io.dnajd.mainservice.service.project_table

import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.domain.project_table.ProjectTableList

interface ProjectTableService {
    fun findAll(includeIssues: Boolean = false): ProjectTableList

    fun getAllByProjectId(projectId: Long, includeIssues: Boolean = false): ProjectTableDtoList

    fun findById(id: Long, includeIssues: Boolean = false): ProjectTable

    fun getById(id: Long, includeIssues: Boolean = false): ProjectTableDto

    fun createTable(projectId: Long, dto: ProjectTableDto): ProjectTableDto

    fun updateTable(id: Long, dto: ProjectTableDto): ProjectTableDto

    fun swapTablePositions(fId: Long, sId: Long)

    fun deleteById(id: Long)
}
