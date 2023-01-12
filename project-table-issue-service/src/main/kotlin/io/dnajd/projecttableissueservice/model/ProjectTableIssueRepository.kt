package io.dnajd.projecttableissueservice.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjectTableIssueRepository: JpaRepository<ProjectTableIssue, Long> {

    fun findAllByTableId(tableId: Long): List<ProjectTableIssue>

    fun findByIdAndTableProjectId(id: Long, projectTableId: Long): Optional<ProjectTableIssue>

}