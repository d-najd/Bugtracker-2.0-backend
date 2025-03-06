package io.dnajd.mainservice.repository
import io.dnajd.mainservice.domain.project.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    // find by username

    override fun findById(id: Long): Optional<Project>
}