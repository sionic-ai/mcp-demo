package ai.sionic.mcpdemo.repository.exposed

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UrlTable : Table("urls") {
    val shortKey = varchar("short_key", 10).uniqueIndex()
    val originalUrl = text("original_url")
    val createdAt = datetime("created_at")
    
    override val primaryKey = PrimaryKey(shortKey)
}
