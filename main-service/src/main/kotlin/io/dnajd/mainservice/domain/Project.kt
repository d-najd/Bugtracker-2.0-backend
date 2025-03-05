package io.dnajd.mainservice.domain

import com.fasterxml.jackson.annotation.JsonFormat
import io.dnajd.mainservice.infrastructure.domain.BaseEntity
import io.dnajd.mainservice.infrastructure.domain.BaseListEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.validation.constraints.NotEmpty
import java.util.*

@Entity
@Table(name = "project")
class Project(
    @NotEmpty
    var title: String,

    @Column(length = 65535)
    var description: String? = null,

    @JsonFormat(pattern = "yyyy-MM-d HH:mm:ss")
    @NotEmpty
    var createdAt: Date = Date(),
) : BaseEntity()

class ProjectList(data: List<Project>) : BaseListEntity<Project>(data)
