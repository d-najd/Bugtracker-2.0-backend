package io.dnajd.mainservice.service.project

import io.dnajd.mainservice.domain.project.*

interface ProjectService {
    fun findAll(): List<Project>

    fun getAllByUsername(username: String): ProjectDtoList

    fun find(id: Long): Project

    fun get(id: Long): ProjectDto

    fun create(projectDto: ProjectDto): ProjectDto

    fun update(id: Long, projectDto: ProjectDto): ProjectDto

    fun delete(id: Long)
}