package io.dnajd.mainservice.infrastructure.domain

import jakarta.persistence.*
import java.io.Serializable

@MappedSuperclass
open class BaseEntity : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long = 0L
}