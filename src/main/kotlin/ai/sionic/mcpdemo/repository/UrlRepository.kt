package ai.sionic.mcpdemo.repository

import ai.sionic.mcpdemo.model.UrlEntity

interface UrlRepository {
    fun save(shortKey: String, originalUrl: String): UrlEntity
    fun findByShortKey(shortKey: String): UrlEntity?
    fun findAll(): List<UrlEntity>
    fun delete(shortKey: String)
}
