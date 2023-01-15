package io.dnajd.projecttableissueservice.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProjectTableIssueRepository: JpaRepository<ProjectTableIssue, Long> {

    fun findAllByTableId(tableId: Long): List<ProjectTableIssue>

    fun findByIdAndTableId(id: Long, tableId: Long): Optional<ProjectTableIssue>

}