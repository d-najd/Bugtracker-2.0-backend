package io.dnajd.mainservice.service.project_table

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.domain.project_table.ProjectTableList
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.ProjectTableRepository
import io.dnajd.mainservice.service.project.ProjectServiceImpl
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class ProjectTableServiceImpl(
    private val tableRepository: ProjectTableRepository,
    private val mapper: ShapeShift,
) : ProjectTableService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(includeIssues: Boolean): ProjectTableList {
        val persistedTables = tableRepository.findAll(ProjectTable.entityGraph(includeIssues = includeIssues))

        return ProjectTableList(persistedTables)
    }

    override fun getAllByProjectId(projectId: Long, includeIssues: Boolean): ProjectTableDtoList {
        val persistedTables = tableRepository.findAllByProjectId(
            projectId, ProjectTable.entityGraph(includeIssues = includeIssues)
        )

        return ProjectTableDtoList(mapper.mapCollection(persistedTables))
    }

    override fun findById(id: Long, includeIssues: Boolean): ProjectTable {
        return tableRepository.findById(id, ProjectTable.entityGraph(includeIssues = includeIssues)).orElseThrow {
            log.error("Resource ${ProjectTable::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(ProjectTable::class)
        }
    }

    override fun getById(id: Long, includeIssues: Boolean): ProjectTableDto {
        val persistedTable = findById(id, includeIssues)

        return mapper.map(persistedTable)
    }

    override fun createTable(projectId: Long, dto: ProjectTableDto): ProjectTableDto {
        dto.issues = null

        val tablesInProjectCount = tableRepository.countByProjectId(projectId)
        var transientTable: ProjectTable = mapper.map(dto)
        transientTable = transientTable.copy(
            projectId = projectId,
            position = tablesInProjectCount
        )

        val persistedTable = tableRepository.save(transientTable)
        return mapper.map(persistedTable)
    }

    override fun updateTable(id: Long, dto: ProjectTableDto): ProjectTableDto {
        dto.issues = null

        val persistedTable = findById(id, includeIssues = true)
        val transientTable = mapper.mapChangedFields(persistedTable, dto)

        return mapper.map(tableRepository.saveAndFlush(transientTable))
    }

    override fun swapTablePositions(fId: Long, sId: Long) {
        val firstTable = findById(fId)
        val secondTable = findById(sId)

        if (firstTable.projectId != secondTable.projectId) {
            log.error("Tables don't belong to the same project $fId $sId")
            throw IllegalArgumentException("Tables don't belong to the same project $fId $sId")
        }

        tableRepository.swapPositions(fId.toInt(), sId.toInt())
    }

    override fun deleteById(id: Long) {
        val persistedTable = findById(id)

        tableRepository.delete(persistedTable)
        tableRepository.moveToLeftAfter(persistedTable.projectId, persistedTable.position)
    }
}