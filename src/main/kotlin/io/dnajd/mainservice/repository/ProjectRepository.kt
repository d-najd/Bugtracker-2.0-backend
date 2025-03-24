package io.dnajd.mainservice.repository
import io.dnajd.mainservice.domain.project.Project
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProjectRepository : JpaRepository<Project, Long> {
    override fun findById(id: Long): Optional<Project>
}