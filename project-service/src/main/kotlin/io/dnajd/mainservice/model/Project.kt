package io.dnajd.mainservice.model

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.Hibernate
import java.util.*

@Entity
@Table(name = "project")
data class Project (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id: Long = -1L,

    @Transient
    var owner: String? = null, // TODO, add this field using userAuthorityService possibly using transient value

    @Column(name = "title")
    @NotEmpty
    var title: String,

    @Column(name = "description", length = 65535)
    var description: String? = null,

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var createdAt: Date = Date(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Project

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , title = $title , description = $description , createdAt = $createdAt )"
    }
}

/**
 * maps owners to a list of projects, this action modifies the original list
 *
 * @param owners list of project owners, must contain [UserAuthorityType.project_owner] for every given project in List<Project>
 * @throws NullPointerException if [UserAuthorityType.project_owner] does not exist for a project
 */
fun List<Project>.mapOwners(owners: List<UserAuthority>) = forEach { project -> project.owner = owners.find {
        it.projectId == project.id && it.authority == UserAuthorityType.project_owner
    }!!.username }