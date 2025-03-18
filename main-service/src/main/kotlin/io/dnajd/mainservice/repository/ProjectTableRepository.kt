package io.dnajd.mainservice.repository

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository
import io.dnajd.mainservice.domain.project.Project
import io.dnajd.mainservice.domain.project_table.ProjectTable
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.query.Procedure
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProjectTableRepository : EntityGraphJpaRepository<ProjectTable, Long> {
    override fun findAll(entityGraph: EntityGraph): MutableList<ProjectTable>

    fun findAllByProjectId(projectId: Long, entityGraph: EntityGraph = EntityGraph.NOOP): MutableList<ProjectTable>

    // fun findAllByProjectId(projectId: Long): MutableList<ProjectTable>

    fun countByProjectId(projectId: Long): Int

    /*
        "UPDATE ProjectTable pt " +
                "SET pt.position = CASE WHEN " +
                "pt.position = :fPos THEN :sPos ELSE :fPos END WHERE pt.position IN (:fPos, :sPos)"

     */


    /**
     * No checking is done to check if the id's belong to the same [Project] here
     */
    @Query(value = "CALL project_table_swap_positions(:fId, :sId);", nativeQuery = true)
    @Modifying
    @Transactional
    fun swapPositions(
        @Param("fId") sId: Int,
        @Param("sId") fId: Int,
    )

    /**
     * Use after table has been removed, moves all the project tables in given project one spot to the left after the
     * given position
     */
    @Query(
        "UPDATE ProjectTable pt " +
                "SET pt.position = pt.position - 1 " +
                "WHERE pt.projectId = :projectId AND pt.position > :position"
    )
    @Modifying
    @Transactional
    fun moveToLeftAfter(@Param("projectId") projectId: Long, @Param("position") position: Int)
}