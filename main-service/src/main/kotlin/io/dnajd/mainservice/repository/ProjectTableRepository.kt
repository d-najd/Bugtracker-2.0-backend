package io.dnajd.mainservice.repository

import io.dnajd.mainservice.domain.project_table.ProjectTable
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProjectTableRepository: JpaRepository<ProjectTable, Long> {
    fun findAllByProjectId(projectId: Long): List<ProjectTable>

    @Query(
        "UPDATE ProjectTable pt " +
                "SET pt.position = CASE WHEN " +
                "pt.position = :fPos THEN :sPos ELSE :fPos END WHERE pt.position IN (:fPos, :sPos)"
    )
    @Modifying
    @Transactional
    fun swapPositions(
        @Param("fPos") fPos: Int,
        @Param("sPos") sPos: Int,
    )

    fun countByProjectId(projectId: Long): Int
}