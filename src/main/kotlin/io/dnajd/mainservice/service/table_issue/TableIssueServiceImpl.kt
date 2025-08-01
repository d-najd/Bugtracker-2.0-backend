package io.dnajd.mainservice.service.table_issue

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.infrastructure.mapper.mapChangedFields
import io.dnajd.mainservice.repository.TableIssueRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
@Transactional
class TableIssueServiceImpl(
    private val repository: TableIssueRepository,
    private val mapper: ShapeShift,
) : TableIssueService {
    override fun getAllByTableId(tableId: Long, includeChildIssues: Boolean): TableIssueDtoList {
        val persistedIssues = repository.findAllByTableId(tableId, TableIssue.entityGraph(includeChildIssues))

        return TableIssueDtoList(mapper.mapCollection(persistedIssues))
    }

    override fun get(
        id: Long,
        includeChildIssues: Boolean,
        includeAssigned: Boolean,
        includeComments: Boolean,
        includeLabels: Boolean
    ): TableIssueDto {
        val entity = repository.getReferenceById(
            id,
            TableIssue.entityGraph(
                includeChildIssues,
                includeAssigned,
                includeComments,
                includeLabels
            )
        )
        return mapper.map(entity)
    }

    override fun issuesBelongToSameTable(fId: Long, sId: Long): Boolean {
        val firstIssue = repository.getReferenceById(fId)
        val secondIssue = repository.getReferenceById(sId)

        return firstIssue.tableId == secondIssue.tableId
    }

    override fun create(tableId: Long, reporterUsername: String, dto: TableIssueDto): TableIssueDto {
        val issuesInTableCount = repository.countByTableId(tableId)

        var transientIssue: TableIssue = mapper.map(dto)
        transientIssue = transientIssue.copy(
            tableId = tableId,
            reporter = reporterUsername,
            position = issuesInTableCount
        )

        val persistedIssue = repository.save(transientIssue)
        return mapper.map(persistedIssue)
    }

    override fun update(id: Long, dto: TableIssueDto): TableIssueDto {
        val persistedIssue = repository.getReferenceById(id)
        val transientIssue = mapper.mapChangedFields(persistedIssue, dto)

        return mapper.map(repository.saveAndFlush(transientIssue))
    }

    override fun swapIssuePositions(fId: Long, sId: Long): TableIssueDtoList {
        if (!issuesBelongToSameTable(fId, sId)) {
            throw IllegalArgumentException("Issues don't belong to the same table $fId $sId")
        }

        repository.swapPositions(fId, sId)
        val updatedIssues = repository.findAllById(listOf(fId, sId))
        return TableIssueDtoList(mapper.mapCollection(updatedIssues))
    }

    override fun movePositionTo(fId: Long, sId: Long): TableIssueDtoList {
        if (!issuesBelongToSameTable(fId, sId)) {
            throw IllegalArgumentException("Issues don't belong to the same table $fId $sId")
        }

        val fIssue = repository.getReferenceById(fId)

        repository.movePositionTo(fId, sId)
        val issuesInTable = repository.findAllByTableId(fIssue.tableId)
        return TableIssueDtoList(mapper.mapCollection(issuesInTable))
    }

    override fun moveToTable(id: Long, tableId: Long): TableIssueDtoList {
        if (!repository.taskAndTableBelongToSameProject(id, tableId)) {
            throw IllegalArgumentException("Task $id and table $tableId don't belong to the same project")
        }

        val originalIssue = repository.getReferenceById(id)
        if (originalIssue.tableId == tableId) {
            throw IllegalArgumentException("Trying to change table for a issue but putting the current table? taskId: $id tableId: $tableId")
        }

        val issuesInTableCount = repository.countByTableId(tableId)
        val modifiedIssue = originalIssue.copy(
            tableId = tableId,
            position = issuesInTableCount
        )

        val persistedIssue = repository.save(modifiedIssue)
        repository.moveToLeftAfter(originalIssue.tableId, originalIssue.position)

        val issuesInOriginalTable = repository.findAllByTableId(originalIssue.tableId)
        val issuesInNewTable = repository.findAllByTableId(persistedIssue.tableId)
        val issuesCombined = issuesInOriginalTable.plus(issuesInNewTable)

        return TableIssueDtoList(mapper.mapCollection(issuesCombined))
    }

    override fun setParentIssue(id: Long, parentIssueId: Long) {
        if (!repository.tasksBelongToSameProject(id, parentIssueId)) {
            throw IllegalArgumentException("Trying to set parent issue to task that doesn't belong to the same project? id: $id, parentId $parentIssueId")
        }

        val persistedParentIssue = repository.getReferenceById(parentIssueId)
        if (persistedParentIssue.parentIssueId != null) {
            throw IllegalArgumentException("Parent issue must not have its own parent issue. id: $id, parentId: $parentIssueId")
        }

        val originalIssue = repository.getReferenceById(id)
        val transientIssue = originalIssue.copy(
            parentIssueId = parentIssueId
        )

        repository.saveAndFlush(transientIssue)
    }

    override fun delete(id: Long): TableIssueDtoList {
        val persistedTable = repository.getReferenceById(id)

        repository.delete(persistedTable)
        repository.moveToLeftAfter(persistedTable.tableId, persistedTable.position)

        val modifiedIssues = repository.findByTableIdAndPositionGreaterThanEqual(persistedTable.tableId, persistedTable.position)

        return TableIssueDtoList(mapper.mapCollection(modifiedIssues))
    }
}