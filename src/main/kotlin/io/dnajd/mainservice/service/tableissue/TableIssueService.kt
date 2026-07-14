package io.dnajd.mainservice.service.tableissue

import io.dnajd.mainservice.domain.tableissue.TableIssueDto
import io.dnajd.mainservice.domain.tableissue.TableIssueDtoList

interface TableIssueService {
	fun getAllByTableId(
		tableId: Long,
		includeChildIssues: Boolean = false,
	): TableIssueDtoList

	fun get(
		id: Long,
		includeChildIssues: Boolean = false,
		includeAssigned: Boolean = false,
		includeComments: Boolean = false,
		includeLabels: Boolean = false,
	): TableIssueDto

	fun issuesBelongToSameTable(
		fId: Long,
		sId: Long,
	): Boolean

	fun create(
		tableId: Long,
		reporterUsername: String,
		dto: TableIssueDto,
	): TableIssueDto

	fun update(
		id: Long,
		dto: TableIssueDto,
	): TableIssueDto

	/**
	 * @return both of the tasks that were swapped
	 */
	fun swapIssuePositions(
		fId: Long,
		sId: Long,
	): TableIssueDtoList

	/**
	 * @return returns the tasks in the table
	 */
	fun movePositionTo(
		fId: Long,
		sId: Long,
	): TableIssueDtoList

	/**
	 * @return returns the tasks from the original and new table
	 */
	fun moveToTable(
		id: Long,
		tableId: Long,
	): TableIssueDtoList

	fun setParentIssue(
		id: Long,
		parentIssueId: Long,
	)

	/**
	 * @return list of modified issues due to position change
	 */
	fun delete(id: Long): TableIssueDtoList
}
