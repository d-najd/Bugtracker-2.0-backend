package io.dnajd.userauthorityservice.web

import io.dnajd.userauthorityservice.model.UserAuthority
import io.dnajd.userauthorityservice.model.UserAuthorityIdentity
import io.dnajd.userauthorityservice.model.UserAuthorityRepository
import io.dnajd.userauthorityservice.model.UserAuthorityType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api")
@RestController
class UserAuthorityResource(val repository: UserAuthorityRepository) {
    @GetMapping("/testing/getAll")
    fun getAll(): UserAuthorityHolder {
        return UserAuthorityHolder(repository.findAll())
    }

    @PostMapping
    fun post(
        @RequestBody pojo: UserAuthority,
    ): UserAuthority {
        if(pojo.authority == UserAuthorityType.project_owner){
            if(repository.findByProjectIdAndAuthority(
                authority = UserAuthorityType.project_owner,
                projectId = pojo.projectId,
            ).isNotEmpty()) {
                throw IllegalArgumentException("project can't have more than one owner")
            }
        }
        return repository.save(pojo.copy(
                identity = UserAuthorityIdentity(
                    username = pojo.username,
                    projectId = pojo.projectId,
                    authority = pojo.authority,
                )
            )
        )
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    fun delete(
        @RequestBody id: UserAuthorityIdentity,
    ) {
        repository.deleteById(id)
    }
}
