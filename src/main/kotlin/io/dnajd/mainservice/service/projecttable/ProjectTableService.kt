package io.dnajd.mainservice.service.projecttable

import io.dnajd.mainservice.domain.projecttable.ProjectTableDto
import io.dnajd.mainservice.domain.projecttable.ProjectTableDtoList

interface ProjectTableService {
	fun getAllByProjectId(
		projectId: Long,
		includeIssues: Boolean = false,
	): ProjectTableDtoList

	fun get(
		id: Long,
		includeIssues: Boolean = false,
	): ProjectTableDto

	fun create(
		projectId: Long,
		dto: ProjectTableDto,
	): ProjectTableDto

	fun update(
		id: Long,
		dto: ProjectTableDto,
	): ProjectTableDto

	/**
	 * @return returns the swapped tables
	 */
	fun swapTablePositions(
		fId: Long,
		sId: Long,
	): ProjectTableDtoList

	/**
	 * @return returns list of modified tables due to position change
	 */
	fun delete(id: Long): ProjectTableDtoList
}
