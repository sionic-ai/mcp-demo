package ai.sionic.mcpdemo.model

import java.time.LocalDateTime

data class UrlEntity(
    val shortKey: String,
    val originalUrl: String,
    val createdAt: LocalDateTime
)
