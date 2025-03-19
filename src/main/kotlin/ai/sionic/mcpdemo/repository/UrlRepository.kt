package ai.sionic.mcpdemo.repository

import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class UrlRepository {
    private val urlMap = mutableMapOf<String, UrlEntity>()

    fun save(shortKey: String, originalUrl: String): UrlEntity {
        val entity = UrlEntity(shortKey, originalUrl, LocalDateTime.now())
        urlMap[shortKey] = entity
        return entity
    }

    fun findByShortKey(shortKey: String): UrlEntity? {
        return urlMap[shortKey]
    }

    fun findAll(): List<UrlEntity> {
        return urlMap.values.toList()
    }

    fun delete(shortKey: String) {
        urlMap.remove(shortKey)
    }
}

data class UrlEntity(
    val shortKey: String,
    val originalUrl: String,
    val createdAt: LocalDateTime
)
