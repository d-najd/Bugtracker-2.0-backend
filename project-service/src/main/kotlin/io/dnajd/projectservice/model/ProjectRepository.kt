package io.dnajd.projectservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository: JpaRepository<Project, Long> {

    fun findAllById(id: Long): List<Project>

}