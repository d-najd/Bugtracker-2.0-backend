package io.dnajd.mainservice.service.project_table

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.ProjectTableRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class ProjectTableServiceImpl(
    private val repository: ProjectTableRepository,
    private val mapper: ShapeShift,
) : ProjectTableService {
    override fun getAllByProjectId(projectId: Long, includeIssues: Boolean): ProjectTableDtoList {
        val graph = ProjectTable.entityGraph(includeIssues = includeIssues)
        val persistedTables = repository.findByProjectId(
            projectId, graph
        )

        return ProjectTableDtoList(mapper.mapCollection(persistedTables))
    }

    override fun get(id: Long, includeIssues: Boolean): ProjectTableDto {
        val persistedTable = repository.findById(id, ProjectTable.entityGraph(includeIssues = includeIssues)).get()

        return mapper.map(persistedTable)
    }

    override fun create(projectId: Long, dto: ProjectTableDto): ProjectTableDto {
        val tablesInProjectCount = repository.countByProjectId(projectId)
        var transientTable: ProjectTable = mapper.map(dto)
        transientTable = transientTable.copy(
            projectId = projectId,
            position = tablesInProjectCount
        )

        val persistedTable = repository.save(transientTable)
        return mapper.map(persistedTable)
    }

    override fun update(id: Long, dto: ProjectTableDto): ProjectTableDto {
        val dtoValidated = dto.copy(issues = null)

        val persistedTable = repository.findById(id, ProjectTable.entityGraph(includeIssues = true)).get()
        val transientTable = mapper.mapChangedFields(persistedTable, dtoValidated)

        return mapper.map(repository.saveAndFlush(transientTable))
    }

    override fun swapTablePositions(fId: Long, sId: Long): ProjectTableDtoList {
        val firstTable = repository.getReferenceById(fId)
        val secondTable = repository.getReferenceById(sId)

        if (firstTable.projectId != secondTable.projectId) {
            throw IllegalArgumentException("Tables don't belong to the same project $fId $sId")
        }

        repository.swapPositions(fId, sId)
        val updatedTables = repository.findAllById(listOf(fId, sId))
        return ProjectTableDtoList(mapper.mapCollection(updatedTables))
    }

    override fun delete(id: Long) {
        val persistedTable = repository.getReferenceById(id)

        repository.delete(persistedTable)
        repository.moveToLeftAfter(persistedTable.projectId, persistedTable.position)
    }
}