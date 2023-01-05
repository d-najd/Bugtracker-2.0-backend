package io.dnajd.projecttableissueservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

@Entity
@Table(name = "project_table")
class ProjectTable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    var id = -1L

    @JsonIgnore
    @OneToMany(
        cascade = [(CascadeType.REMOVE)],
        mappedBy = "table",
        fetch = FetchType.LAZY
    )
    val tables: MutableList<ProjectTableParentIssue> = mutableListOf()
}
