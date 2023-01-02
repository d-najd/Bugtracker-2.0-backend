package io.dnajd.projectservice.model

import jakarta.persistence.*
import java.util.*
import javax.validation.constraints.NotEmpty

@Entity
@Table(name = "project")
class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id = 0L

    @Column(name = "owner")
    var owner = ""

    @Column(name = "title")
    @NotEmpty
    var title = ""

    @Column(name = "description")
    var description: String? = null

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var createdAt: Date = Date()
}