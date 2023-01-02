package io.dnajd.projectservice.model

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository: JpaRepository<Project, Long> {

    fun findAllByOwner(owner: String): List<Project>

}