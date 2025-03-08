package io.dnajd.mainservice.controller

import io.dnajd.mainservice.infrastructure.Endpoints
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Endpoints.PROJECT_TABLE)
class ProjectTableController {
    @GetMapping("/testing/getAll")
    fun findAll() {

    }
}