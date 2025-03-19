package ai.sionic.mcpdemo.model

import kotlinx.serialization.Serializable

@Serializable
data class UrlDto(
    val shortKey: String,
    val originalUrl: String,
    val createdAt: String
)

@Serializable
data class ShortenUrlRequest(
    val url: String
)

@Serializable
data class ShortenUrlResponse(
    val shortUrl: String,
    val shortKey: String
)
