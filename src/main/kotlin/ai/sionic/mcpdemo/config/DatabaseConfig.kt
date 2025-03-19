package ai.sionic.mcpdemo.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@Profile("local_postgres")
class DatabaseConfig {
    private val logger = KotlinLogging.logger {}

    @Bean
    fun dataSource(): DataSource {
        logger.info { "Setting up PostgreSQL DataSource" }
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.url = "jdbc:postgresql://localhost:5432/mcp_demo_db"
        dataSource.username = "postgres"
        dataSource.password = "password1"
        return dataSource
    }

    @Bean
    fun database(dataSource: DataSource): Database {
        logger.info { "Initializing Exposed with PostgreSQL database" }
        return Database.connect(dataSource)
    }
}
