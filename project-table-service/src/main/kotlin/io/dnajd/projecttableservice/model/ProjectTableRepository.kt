package io.dnajd.projecttableservice.model

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ProjectTableRepository: JpaRepository<ProjectTable, Long> {

    fun findAllByProjectId(projectId: Long): List<ProjectTable>

    fun findByIdAndProjectId(id: Long, projectId: Long): Optional<ProjectTable>
}

