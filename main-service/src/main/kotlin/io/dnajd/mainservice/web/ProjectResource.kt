package io.dnajd.mainservice.web

import org.springframework.web.bind.annotation.*

@RequestMapping("/api")
@RestController
class ProjectResource() {
    @GetMapping("/testing/getAll")
    fun getAll(): String {
        return "TEST"
    }
}
