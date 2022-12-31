package io.dnajd.projecttableservice.model

import jakarta.persistence.*
import java.util.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "project_table")
class ProjectTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id = 0L

    @Column(name = "project_id")
    var projectId = 0L

    @Column(name = "title")
    @NotEmpty
    var title = ""

    @Column(name = "position")
    var position = -1
}