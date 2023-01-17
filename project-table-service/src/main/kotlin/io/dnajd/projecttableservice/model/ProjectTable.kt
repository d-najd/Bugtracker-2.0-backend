package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.Hibernate
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "project_table",
    uniqueConstraints = [
        /*
          maintaining this proved to be more pain than its worth since you CANT SWAP VALUES BETWEEN ROWS which forces the
          position to be temporarily set to -1 which leads to more problems and breaking points which
          defeats the whole purpose of the constraint
         */
        // UniqueConstraint(name = "project_table_unique_1", columnNames = ["projectId", "position"]),
        UniqueConstraint(name = "project_table_unique_2", columnNames = ["projectId", "title"])
    ]
)
data class ProjectTable (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id: Long = -1L,

    @JsonIgnore
    @Column(name = "projectId")
    var projectId: Long = -1L,

    @NotEmpty
    @Column(name = "title")
    var title: String = "",

    @Column(name = "position")
    var position: Int = -1,

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
        name = "projectId",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var project: Project? = null,

    @OneToMany(
        cascade = [(CascadeType.REMOVE)],
        mappedBy = "table",
        fetch = FetchType.LAZY
    )
    val issues: MutableList<ProjectTableIssue> = mutableListOf(),
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
        return this::class.simpleName + "(id = $id , projectId = $projectId , title = $title , position = $position )"
    }
}
