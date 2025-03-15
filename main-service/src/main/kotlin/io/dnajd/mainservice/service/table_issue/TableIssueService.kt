package io.dnajd.mainservice.service.table_issue

import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import io.dnajd.mainservice.domain.table_issue.TableIssueList
import org.springframework.web.bind.annotation.PathVariable

interface TableIssueService {
    fun findAll(): TableIssueList

    fun getAllByTableId(tableId: Long, includeChildIssues: Boolean = false): TableIssueDtoList

    fun findById(
        id: Long,
        includeChildIssues: Boolean = false,
        includeAssigned: Boolean = false,
        includeComments: Boolean = false,
        includeLabels: Boolean = false
    ): TableIssue

    fun getById(
        id: Long,
        includeChildIssues: Boolean = false,
        includeAssigned: Boolean = false,
        includeComments: Boolean = false,
        includeLabels: Boolean = false
    ): TableIssueDto

    fun createIssue(tableId: Long, reporterUsername: String, dto: TableIssueDto): TableIssueDto

    fun updateIssue(@PathVariable id: Long, dto: TableIssueDto): TableIssueDto

    fun swapIssuePositions(fId: Long, sId: Long)

    fun changeTable(id: Long, tableId: Long): Int

    fun setParentIssue(id: Long, parentIssueId: Long)

    fun deleteIssue(id: Long)
}