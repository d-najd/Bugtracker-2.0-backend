package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.Hibernate

@Entity
@Table(
    name = "project_label",
    /*
    uniqueConstraints = [
        UniqueConstraint(name = "project_label_unique_1", columnNames = ["id", "project_id"]),
    ]
     */
)
data class ProjectLabel (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id: Long = -1L,

    /*
    @Column(name = "project_id")
    var projectId = -1L
     */

    @Column(name = "label")
    var label: String = "",

    @JsonIgnore
    @ManyToMany(mappedBy = "labels")
    var projectTableIssues: MutableList<ProjectTableIssue> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ProjectLabel

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , label = $label )"
    }
}
