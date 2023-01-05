package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(
    name = "project_label",
    /*
    uniqueConstraints = [
        UniqueConstraint(name = "project_label_unique_1", columnNames = ["id", "project_id"]),
    ]
     */
)
class ProjectLabel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id = -1L

    /*
    @Column(name = "project_id")
    var projectId = -1L
     */

    @Column(name = "label")
    var label = ""

    @JsonIgnore
    @ManyToMany(mappedBy = "labels")
    var projectTableIssues: MutableList<ProjectTableIssue> = mutableListOf()
}
