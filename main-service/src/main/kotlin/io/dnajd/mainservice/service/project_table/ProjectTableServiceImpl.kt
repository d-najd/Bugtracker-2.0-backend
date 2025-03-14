package io.dnajd.mainservice.service.project_table

import com.cosium.spring.data.jpa.entity.graph.domain2.NamedEntityGraph
import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.project_table.ProjectTableDto
import io.dnajd.mainservice.domain.project_table.ProjectTableDtoList
import io.dnajd.mainservice.domain.project_table.ProjectTableList
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
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
        val entityGraph = if (ignoreIssues) {
            NamedEntityGraph.NOOP
        } else {
            NamedEntityGraph.loading("ProjectTable.issues")
        }

        // val persistedTables = tableRepository.findAllByProjectId(projectId, entityGraph)
        val persistedTables = tableRepository.findAllByProjectId(projectId)
        val test: List<ProjectTableDto> = mapper.mapCollection(persistedTables)
        log.error("IS RETRIEVED 0 ${Hibernate.isInitialized(persistedTables[0].issues)}")
        if (!ignoreIssues) {
            test.forEach { o ->
                var p = persistedTables.first { o.id == it.id }
                var issues = p.issues
                var mappedIssues: List<TableIssueDto> = mapper.mapCollection(issues)
                o.issues = mappedIssues
            }

            /*
            test.forEach { o -> o.issues = mapper.mapCollection(persistedTables.first { p ->
                p.id == o.id
            }.issues.toList()) }
             */
            // test.data.forEach { o -> o.issues = mapper.mapCollection(persistedTables.first { p -> p.id == o.id }.issues) }
            // val test1 = ProjectTableDtoList(mapper.mapCollection(persistedTables))
            // persistedTables.forEach { o -> o.issues = emptyList() }
        }

        log.error("IS RETRIEVED 1 ${Hibernate.isInitialized(persistedTables[0].issues)}")
        val test1 = ProjectTableDtoList(mapper.mapCollection(persistedTables))
        log.error("IS RETRIEVED 2 ${Hibernate.isInitialized(persistedTables[0].issues)}")

        return ProjectTableDtoList(test)
    }

    override fun findById(id: Long): ProjectTable {
        return tableRepository.findById(id).orElseThrow {
            log.error("Resource ${ProjectTable::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(ProjectTable::class)
        }
    }

    override fun getById(id: Long, ignoreIssues: Boolean): ProjectTableDto {
        val persistedTable = findById(id)
        if (ignoreIssues) {
            persistedTable.issues = emptyList()
        }
        return mapper.map(persistedTable)
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
        persistedTable.issues = emptyList()
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
        tableRepository.moveToLeftAfter(persistedTable.projectId, persistedTable.position)
    }
}