package ai.sionic.mcpdemo.controller

import ai.sionic.mcpdemo.repository.UrlEntity
import ai.sionic.mcpdemo.service.UrlShortenerService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/shorten")
class UrlShortenerController(private val urlShortenerService: UrlShortenerService) {
    private val logger = KotlinLogging.logger {}

    @PostMapping
    fun shortenUrl(@RequestBody request: ShortenUrlRequest): ResponseEntity<ShortenUrlResponse> {
        logger.info { "Received request to shorten URL: ${request.url}" }
        
        val urlEntity = urlShortenerService.shortenUrl(request.url)
        
        val shortUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/shorten/{shortKey}")
            .buildAndExpand(urlEntity.shortKey)
            .toUriString()
        
        logger.info { "URL shortened successfully. Short URL: $shortUrl" }
        
        return ResponseEntity.ok(ShortenUrlResponse(shortUrl, urlEntity.shortKey))
    }

    @GetMapping("/{shortKey}")
    fun redirectToOriginalUrl(@PathVariable shortKey: String): ResponseEntity<RedirectResponse> {
        logger.info { "Received request to get original URL for short key: $shortKey" }
        
        val originalUrl = urlShortenerService.getOriginalUrl(shortKey)
        
        return if (originalUrl != null) {
            logger.info { "Original URL found: $originalUrl" }
            ResponseEntity.ok(RedirectResponse(originalUrl))
        } else {
            logger.info { "No URL found for short key: $shortKey" }
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/urls")
    fun getAllUrls(): ResponseEntity<List<UrlEntity>> {
        logger.info { "Received request to get all shortened URLs" }
        val urls = urlShortenerService.getAllUrls()
        return ResponseEntity.ok(urls)
    }

    @DeleteMapping("/{shortKey}")
    fun deleteUrl(@PathVariable shortKey: String): ResponseEntity<Void> {
        logger.info { "Received request to delete URL with short key: $shortKey" }
        urlShortenerService.deleteUrl(shortKey)
        return ResponseEntity.noContent().build()
    }
}

data class ShortenUrlRequest(val url: String)

data class ShortenUrlResponse(val shortUrl: String, val shortKey: String)

data class RedirectResponse(val originalUrl: String)
