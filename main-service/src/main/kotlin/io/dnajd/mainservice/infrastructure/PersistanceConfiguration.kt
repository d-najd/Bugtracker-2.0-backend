package io.dnajd.mainservice.infrastructure

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
public class PersistanceConfiguration {

    @Bean
    fun cleanMigrateStrategy(): FlywayMigrationStrategy {
        return FlywayMigrationStrategy() { flyway ->
            flyway.repair()
            flyway.migrate()
        }
    }
    /*
    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy() {
        FlywayMigrationStrategy strategy = new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                flyway.repair();
                flyway.migrate();
            }
        };
        return strategy;
    }
     */

}
