package ai.sionic.mcpdemo.service

import ai.sionic.mcpdemo.repository.UrlEntity
import ai.sionic.mcpdemo.repository.UrlRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class UrlShortenerService(private val urlRepository: UrlRepository) {
    private val logger = KotlinLogging.logger {}

    fun shortenUrl(originalUrl: String): UrlEntity {
        logger.info { "Shortening URL: $originalUrl" }
        val shortKey = generateUniqueShortKey()
        return urlRepository.save(shortKey, originalUrl)
    }

    fun getOriginalUrl(shortKey: String): String? {
        logger.info { "Looking up original URL for short key: $shortKey" }
        return urlRepository.findByShortKey(shortKey)?.originalUrl
    }

    fun getAllUrls(): List<UrlEntity> {
        logger.info { "Retrieving all shortened URLs" }
        return urlRepository.findAll()
    }

    fun deleteUrl(shortKey: String) {
        logger.info { "Deleting URL with short key: $shortKey" }
        urlRepository.delete(shortKey)
    }

    private fun generateUniqueShortKey(): String {
        val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val letters = ('a'..'z') + ('A'..'Z')
        
        var shortKey: String
        do {
            // 문자로 시작하는 랜덤 6자리 문자열 생성
            val firstChar = letters.random().toString()
            val remainingChars = (1..5)
                .map { allowedChars.random() }
                .joinToString("")
            shortKey = firstChar + remainingChars
        } while (urlRepository.findByShortKey(shortKey) != null)

        logger.info { "Generated short key: $shortKey" }
        return shortKey
    }
}
