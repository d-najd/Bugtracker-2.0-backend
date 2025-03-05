package io.dnajd.mainservice.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository: JpaRepository<Project, Long> {

    // fun findAllByOwner(owner: String): List<Project>

    fun findByIdIn(ids: List<Long>): List<Project>

}