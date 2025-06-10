package io.dnajd.mainservice.repository

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository
import io.dnajd.mainservice.domain.project_authority.ProjectAuthority
import io.dnajd.mainservice.domain.project_authority.ProjectAuthorityIdentity
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProjectAuthorityRepository : EntityGraphJpaRepository<ProjectAuthority, ProjectAuthorityIdentity> {
    fun findByUsernameAndProjectId(username: String, projectId: Long): MutableList<ProjectAuthority>

    @Query("SELECT pa FROM ProjectAuthority AS pa" +
            " INNER JOIN ProjectTable AS pt ON pt.projectId = pa.projectId" +
            " WHERE pa.username = :username AND pt.id = :tableId")
    fun findByUsernameAndProjectTableId(username: String, tableId: Long): MutableList<ProjectAuthority>
}
