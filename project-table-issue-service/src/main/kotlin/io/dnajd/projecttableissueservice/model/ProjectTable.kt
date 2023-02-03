package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(name = "project_table")
data class ProjectTable (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id: Long = -1L,

    @Column(name = "projectId")
    var projectId: Long = -1L,

    @JsonIgnore
    @OneToMany(
        cascade = [(CascadeType.REMOVE)],
        mappedBy = "table",
        fetch = FetchType.LAZY
    )
    val issues: MutableList<ProjectTableChildIssue> = mutableListOf(),
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectTable

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

}
