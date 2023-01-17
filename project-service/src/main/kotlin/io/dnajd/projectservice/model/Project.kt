package io.dnajd.projectservice.model

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

    @Column(name = "owner")
    var owner: String = "",

    @Column(name = "title")
    @NotEmpty
    var title: String = "",

    @Column(name = "description", length = 65535)
    var description: String? = null,

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
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
        return this::class.simpleName + "(id = $id , owner = $owner , title = $title , description = $description , createdAt = $createdAt )"
    }

}