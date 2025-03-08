package io.dnajd.mainservice.service.project_table

import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.domain.project_table.ProjectTableList

interface ProjectTableService {
    fun findAll(): ProjectTableList

    fun getAllByProjectId(projectId: Long, ignoreIssues: Boolean): ProjectTableDtoList

    fun findById(id: Long): ProjectTable

    fun getById(id: Long, ignoreIssues: Boolean): ProjectTableDto

    fun createTable(projectId: Long, dto: ProjectTableDto): ProjectTableDto

    fun updateTable(id: Long, dto: ProjectTableDto): ProjectTableDto

    fun swapTablePositions(fId: Long, sId: Long)

    fun deleteTable(id: Long)
}
