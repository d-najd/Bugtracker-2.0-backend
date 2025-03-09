package io.dnajd.mainservice.repository

import io.dnajd.mainservice.domain.table_issue.TableIssue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TableIssueRepository : JpaRepository<TableIssue, Long> {
    fun findAllByTableId(tableId: Long): MutableList<TableIssue>

    fun countByTableId(tableId: Long): Int
}
