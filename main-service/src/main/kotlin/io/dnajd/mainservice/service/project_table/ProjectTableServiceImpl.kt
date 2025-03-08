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
import org.hibernate.Hibernate
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

    override fun findAll(): ProjectTableList {
        return ProjectTableList(tableRepository.findAll())
    }

    override fun getAllByProjectId(projectId: Long, ignoreIssues: Boolean): ProjectTableDtoList {
        val persistedTables = tableRepository.findAllByProjectId(projectId)

        val test = ProjectTableDtoList(mapper.mapCollection(persistedTables))
        log.error("IS RETRIEVED ${Hibernate.isInitialized(persistedTables[0].project)}")

        return ProjectTableDtoList(mapper.mapCollection(persistedTables))
    }

    override fun findById(id: Long): ProjectTable {
        return tableRepository.findById(id).orElseThrow {
            log.error("Resource ${ProjectTable::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(ProjectTable::class)
        }
    }

    override fun getById(id: Long, ignoreIssues: Boolean): ProjectTableDto {
        return mapper.map(findById(id))
    }

    override fun createTable(projectId: Long, dto: ProjectTableDto): ProjectTableDto {
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
        val persistedTable = findById(id)
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

        tableRepository.swapPositions(firstTable.position, secondTable.position)
    }

    override fun deleteTable(id: Long) {
        val persistedTable = findById(id)
        tableRepository.delete(persistedTable)
    }
}