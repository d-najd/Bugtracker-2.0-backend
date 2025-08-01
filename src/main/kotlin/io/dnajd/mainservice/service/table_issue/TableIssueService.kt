package io.dnajd.mainservice.service.table_issue

import io.dnajd.mainservice.domain.table_issue.TableIssue
import io.dnajd.mainservice.domain.table_issue.TableIssueDto
import io.dnajd.mainservice.domain.table_issue.TableIssueDtoList
import org.springframework.web.bind.annotation.PathVariable

interface TableIssueService {
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

    /**
     * @return both of the tasks that were swapped
     */
    fun swapIssuePositions(fId: Long, sId: Long): TableIssueDtoList

    /**
     * @return returns the tasks in the table
     */
    fun movePositionTo(fId: Long, sId: Long): TableIssueDtoList

    /**
     * @return returns the tasks from the original and new table
     */
    fun moveToTable(id: Long, tableId: Long): TableIssueDtoList

    fun setParentIssue(id: Long, parentIssueId: Long)

    /**
     * @return list of modified issues due to position change
     */
    fun delete(id: Long): TableIssueDtoList
}