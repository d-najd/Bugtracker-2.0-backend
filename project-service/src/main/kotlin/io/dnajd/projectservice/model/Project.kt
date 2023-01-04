package io.dnajd.projectservice.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotEmpty
import java.util.*

@Entity
@Table(name = "project")
class Project{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    var id = -1L

    @Column(name = "owner")
    var owner = ""

    @Column(name = "title")
    @NotEmpty
    var title = ""

    @Column(name = "description", length = 65535)
    var description: String? = null

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotEmpty
    var createdAt: Date = Date()
}