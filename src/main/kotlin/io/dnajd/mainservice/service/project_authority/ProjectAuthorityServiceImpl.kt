package io.dnajd.mainservice.service.project_authority

import dev.krud.shapeshift.ShapeShift
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityDtoList
import io.dnajd.mainservice.repository.ProjectAuthorityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProjectAuthorityServiceImpl(
    private val repository: ProjectAuthorityRepository,
    private val mapper: ShapeShift
): ProjectAuthorityService {
    override fun findByUsernameAndProjectId(username: String, projectId: Long): ProjectAuthorityDtoList {
        val persistedAuthorities = repository.findByUsernameAndProjectId(username, projectId)

        return ProjectAuthorityDtoList(mapper.mapCollection(persistedAuthorities))
    }
}