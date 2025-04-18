package io.dnajd.mainservice.repository

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository
import io.dnajd.mainservice.domain.user.User
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : EntityGraphJpaRepository<User, String> {

}
