package io.dnajd.mainservice.service.table_issue

import io.dnajd.mainservice.repository.TableIssueRepository
import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.domain.table_issue.TableIssueList
import io.dnajd.mainservice.infrastructure.exception.ResourceNotFoundException
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.service.project.ProjectServiceImpl
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class TableIssueServiceImpl(
    private val issueRepository: TableIssueRepository,
    private val mapper: ShapeShift,
): TableIssueService {
    companion object {
        private val log = LoggerFactory.getLogger(ProjectServiceImpl::class.java)
    }

    override fun findAll(): TableIssueList {
        return TableIssueList(issueRepository.findAll())
    }

    override fun getAllByTableId(tableId: Long): TableIssueDtoList {
        val persistedIssues = issueRepository.findAllByTableId(tableId)

        return TableIssueDtoList(mapper.mapCollection(persistedIssues))
    }

    override fun findById(id: Long): TableIssue {
        return issueRepository.findById(id).orElseThrow {
            log.error("Resource ${TableIssue::class.simpleName} with id $id not found")
            throw ResourceNotFoundException(TableIssue::class)
        }
    }

    override fun getById(id: Long): TableIssueDto {
        return mapper.map(findById(id))
    }

    override fun createIssue(tableId: Long, reporterUsername: String, dto: TableIssueDto): TableIssueDto {
        val issuesInTableCount = issueRepository.countByTableId(tableId)

        var transientIssue: TableIssue = mapper.map(dto)
        transientIssue = transientIssue.copy(
            tableId = tableId,
            reporter = reporterUsername,
            position = issuesInTableCount
        )

        val persistedIssue = issueRepository.save(transientIssue)
        return mapper.map(persistedIssue)
    }

    override fun updateIssue(id: Long, dto: TableIssueDto): TableIssueDto {
        val persistedIssue = findById(id)
        val transientIssue = mapper.mapChangedFields(persistedIssue, dto)

        return mapper.map(issueRepository.saveAndFlush(transientIssue))
    }

    override fun swapIssuePositions(fId: Long, sId: Long) {
        val firstIssue = findById(fId)
        val secondIssue = findById(sId)

        if (firstIssue.tableId != secondIssue.tableId) {
            log.error("Issues don't belong to the same table $fId $sId")
            throw IllegalArgumentException("Issues don't belong to the same table $fId $sId")
        }

        issueRepository.swapPositions(firstIssue.position, secondIssue.position)
    }

    override fun changeTable(id: Long, tableId: Long): TableIssueDto {
        if (!issueRepository.taskAndTableBelongToSameProject(id, tableId)) {
            val errorText = "Task $id and table $tableId don't belong to the same project"
            log.error(errorText)
            throw IllegalArgumentException(errorText)
        }

        val originalIssue = findById(id)
        if (originalIssue.tableId == tableId) {
            val errorText = "Trying to change table for a issue but putting the current table? taskId: $id tableId: $tableId"
            log.error(errorText)
            throw IllegalArgumentException(errorText)
        }

        val issuesInTableCount = issueRepository.countByTableId(tableId)
        val modifiedIssue = originalIssue.copy(
            tableId = tableId,
            position = issuesInTableCount
        )

        val persistedIssue = issueRepository.saveAndFlush(modifiedIssue)
        issueRepository.moveToLeftAfter(originalIssue.tableId, originalIssue.position)

        return mapper.map(persistedIssue)
    }

    override fun setParentIssue(id: Long, parentIssueId: Long) {
        TODO()
        /*
        if (!issueRepository.tasksBelongToSameProject(id, parentIssueId)) {
            val errorText = "Trying to set parent issue to task that doesn't belong to the same project? id: $id, parentId $parentIssueId"
            log.error(errorText)
            throw IllegalArgumentException(errorText)
        }

        val originalIssue = findById(id)
        val originalParentIssue = findById(parentIssueId)
         */

        // TODO check circular dependency for ex A -> B -> C -> A
        /*
        if (originalParentIssue.parentIssueId == id) {
            val errorText = "Trying to create circular parent dependency id: $id parentIssueId: $parentIssueId"
            log.error(errorText)
            throw IllegalArgumentException(errorText)
        }
         */


        // val modifiedIssue = originalIssue.copy(parentIssueId)


    }

    override fun deleteIssue(id: Long) {
        val persistedTable = findById(id)

        issueRepository.delete(persistedTable)
        issueRepository.moveToLeftAfter(persistedTable.tableId, persistedTable.position)
    }
}