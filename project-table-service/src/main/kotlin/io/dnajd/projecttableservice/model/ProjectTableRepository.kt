package io.dnajd.projecttableservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectTableRepository: JpaRepository<ProjectTable, Long> {

    fun findAllByProjectId(projectId: Long): List<ProjectTable>

}

