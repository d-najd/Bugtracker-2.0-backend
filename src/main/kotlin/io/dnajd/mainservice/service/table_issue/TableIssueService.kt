package io.dnajd.mainservice.service.table_issue

import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import org.springframework.web.bind.annotation.PathVariable

interface TableIssueService {
    fun findAllTesting(): List<TableIssue>

    fun getAllByTableId(tableId: Long, includeChildIssues: Boolean = false): TableIssueDtoList

    fun get(
        id: Long,
        includeChildIssues: Boolean = false,
        includeAssigned: Boolean = false,
        includeComments: Boolean = false,
        includeLabels: Boolean = false
    ): TableIssueDto

    fun issuesBelongToSameTable(fId: Long, sId: Long): Boolean

    fun create(tableId: Long, reporterUsername: String, dto: TableIssueDto): TableIssueDto

    fun update(@PathVariable id: Long, dto: TableIssueDto): TableIssueDto

    fun swapIssuePositions(fId: Long, sId: Long)

    fun movePositionTo(fId: Long, sId: Long)

    fun moveToTable(id: Long, tableId: Long): Int

    fun setParentIssue(id: Long, parentIssueId: Long)

    fun delete(id: Long)
}