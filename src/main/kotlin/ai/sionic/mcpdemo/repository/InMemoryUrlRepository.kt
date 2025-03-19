package ai.sionic.mcpdemo.repository

import ai.sionic.mcpdemo.model.UrlEntity
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
@Profile("local")
class InMemoryUrlRepository : UrlRepository {
    private val urlMap = mutableMapOf<String, UrlEntity>()

    override fun save(shortKey: String, originalUrl: String): UrlEntity {
        val entity = UrlEntity(shortKey, originalUrl, LocalDateTime.now())
        urlMap[shortKey] = entity
        return entity
    }

    override fun findByShortKey(shortKey: String): UrlEntity? {
        return urlMap[shortKey]
    }

    override fun findAll(): List<UrlEntity> {
        return urlMap.values.toList()
    }

    override fun delete(shortKey: String) {
        urlMap.remove(shortKey)
    }
}
