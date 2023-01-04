package io.dnajd.projecttableservice.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*


@Entity
@Table(name = "project")
class Project(
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id = -1L

    @JsonIgnore
    @OneToMany(cascade = [(CascadeType.REMOVE)], mappedBy = "project", fetch = FetchType.LAZY)
    val tables: MutableList<ProjectTable> = mutableListOf()
}