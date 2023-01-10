package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction

@Entity
@Table(
    name = "project_table",
    uniqueConstraints = [
        UniqueConstraint(name = "project_table_unique_1", columnNames = ["projectId", "position"]),
        UniqueConstraint(name = "project_table_unique_2", columnNames = ["projectId", "title"])
    ]
)
class ProjectTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id = -1L

    @JsonIgnore
    @Column(name = "projectId")
    var projectId = -1L

    @NotEmpty
    @Column(name = "title")
    var title = ""

    @Column(name = "position")
    var position = -1

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(
        name = "projectId",
        referencedColumnName = "id",
        updatable = false,
        insertable = false,
    )
    var project: Project? = null

    @OneToMany(
        cascade = [(CascadeType.REMOVE)],
        mappedBy = "table",
        fetch = FetchType.LAZY
    )
    val issues: MutableList<ProjectTableIssue> = mutableListOf()
}
