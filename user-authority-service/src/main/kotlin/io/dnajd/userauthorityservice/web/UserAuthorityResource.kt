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

    @GetMapping("/username/{username}")
    fun getAllByUsername(
        @PathVariable username: String,
    ): UserAuthorityHolder {
        return UserAuthorityHolder(repository.findAllByUsername(username))
    }

    /**
     * gets all projects where the user has a role and if that role is not null gets the authority where the owner is
     * referenced, this is useful for finding who is owner of the given project
     */
    @GetMapping("/username/{username}/includeOwners")
    fun getAllByUsernameAndIncludeOwners(
        @PathVariable username: String,
    ): UserAuthorityHolder {
        val authorityByUsername = repository.findAllByUsername(username).toMutableList()
        authorityByUsername.addAll(
            repository.findAllByAuthorityAndProjectIdIn(
                authority = UserAuthorityType.project_owner,
                projectIds = authorityByUsername
                    .filterNot { it.authority == UserAuthorityType.project_owner }
                    .map { it.projectId },
            )
        )
        return UserAuthorityHolder(authorityByUsername)
    }

    @GetMapping("/projectId/{projectId}")
    fun getAllByProjectId(
        @PathVariable projectId: Long,
    ): UserAuthorityHolder {
        return UserAuthorityHolder(repository.findAllByProjectId(projectId))
    }

    @PostMapping
    fun post(
        @RequestBody pojo: UserAuthority,
    ): UserAuthority {
        if(pojo.authority == UserAuthorityType.project_owner){
            if(repository.findAllByAuthorityAndProjectId(
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
