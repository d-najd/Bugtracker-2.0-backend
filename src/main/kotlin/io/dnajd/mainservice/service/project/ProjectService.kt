package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.project.*

interface ProjectService {
    fun findAllTesting(): List<Project>

    fun getAllByUsername(username: String): ProjectDtoList

    fun get(id: Long): ProjectDto

    fun create(projectDto: ProjectDto): ProjectDto

    fun update(id: Long, projectDto: ProjectDto): ProjectDto

    fun delete(id: Long)
}