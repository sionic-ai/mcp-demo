package ai.sionic.mcpdemo.repository

import ai.sionic.mcpdemo.model.UrlEntity
import ai.sionic.mcpdemo.repository.exposed.UrlTable
import io.github.oshai.kotlinlogging.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@Profile("local_postgres")
class PostgresUrlRepository(database: Database) : UrlRepository {
    private val logger = KotlinLogging.logger {}

    init {
        transaction(database) {
            SchemaUtils.create(UrlTable)
            logger.info { "Created URL table if it doesn't exist" }
        }
    }

    override fun save(shortKey: String, originalUrl: String): UrlEntity {
        val now = LocalDateTime.now()
        
        transaction {
            UrlTable.insert {
                it[UrlTable.shortKey] = shortKey
                it[UrlTable.originalUrl] = originalUrl
                it[createdAt] = now
            }
        }
        
        return UrlEntity(shortKey, originalUrl, now)
    }

    override fun findByShortKey(shortKey: String): UrlEntity? {
        return transaction {
            UrlTable.select { UrlTable.shortKey eq shortKey }
                .mapNotNull { row ->
                    UrlEntity(
                        row[UrlTable.shortKey],
                        row[UrlTable.originalUrl],
                        row[UrlTable.createdAt]
                    )
                }
                .singleOrNull()
        }
    }

    override fun findAll(): List<UrlEntity> {
        return transaction {
            UrlTable.selectAll()
                .map { row ->
                    UrlEntity(
                        row[UrlTable.shortKey],
                        row[UrlTable.originalUrl],
                        row[UrlTable.createdAt]
                    )
                }
        }
    }

    override fun delete(shortKey: String) {
        transaction {
            UrlTable.deleteWhere { UrlTable.shortKey eq shortKey }
        }
    }
}
