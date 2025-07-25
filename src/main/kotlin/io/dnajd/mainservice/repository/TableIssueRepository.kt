package io.dnajd.mainservice.repository

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository
import io.dnajd.mainservice.domain.project_table.ProjectTable
import io.dnajd.mainservice.domain.table_issue.TableIssue
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TableIssueRepository : EntityGraphJpaRepository<TableIssue, Long> {
    fun getReferenceById(id: Long, entityGraph: EntityGraph): TableIssue

    fun findAllByTableId(tableId: Long, entityGraph: EntityGraph = EntityGraph.NOOP): List<TableIssue>

    fun countByTableId(tableId: Long): Int

    /**
     * No checking is done to check if the id's belong to the same [ProjectTable] here
     */
    @Query("CALL project_table_issue_swap_positions(:fId, :sId);", nativeQuery = true)
    @Modifying
    @Transactional
    fun swapPositions(
        @Param("fId") fId: Long,
        @Param("sId") sId: Long,
    )

    @Query(
        "SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
                "FROM TableIssue t " +
                "WHERE t.id = :taskId " +
                "AND t.table.project.id = (SELECT tb.project.id FROM ProjectTable tb WHERE tb.id = :tableId)"
    )
    fun taskAndTableBelongToSameProject(
        @Param("taskId") taskId: Long,
        @Param("tableId") tableId: Long,
    ): Boolean

    @Query(
        "SELECT CASE WHEN COUNT(firstIssue) > 0 THEN true ELSE FALSE END " +
                "FROM TableIssue firstIssue " +
                "WHERE firstIssue.id = :fId " +
                "AND firstIssue.table.projectId = " +
                "(SELECT secondIssue.table.projectId FROM TableIssue secondIssue WHERE secondIssue.id = :sId)"
    )
    fun tasksBelongToSameProject(
        @Param("fId") fId: Long,
        @Param("sId") sId: Long,
    ): Boolean


    @Query(value = "CALL project_table_issue_move_issue(:fId, :sId);", nativeQuery = true)
    @Modifying
    @Transactional
    fun movePositionTo(
        @Param("fId") fId: Long,
        @Param("sId") sId: Long,
    );

    /**
     * Use after table has been removed, moves all the project tables in given project one spot to the left after the
     * given position
     */
    @Query(
        "UPDATE TableIssue i " +
                "SET i.position = i.position - 1 " +
                "WHERE i.tableId = :tableId AND i.position > :position"
    )
    @Modifying
    @Transactional
    fun moveToLeftAfter(@Param("tableId") tableId: Long, @Param("position") position: Int)

    fun findByTableIdAndPositionGreaterThanEqual(tableId: Long, positionIsGreaterThan: Int): List<TableIssue>
}
