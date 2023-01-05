package io.dnajd.projecttableissueservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectTableIssueRepository: JpaRepository<ProjectTableIssue, Long> {

    fun findAllByTableId(tableId: Long): List<ProjectTableIssue>

}