package io.dnajd.mainservice.infrastructure

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PersistenceConfiguration {
    @Bean
    fun cleanMigrateStrategy(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy() { flyway ->
            flyway.repair()
            flyway.migrate()
        }
    }
}
