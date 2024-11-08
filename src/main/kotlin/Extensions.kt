package org.example

import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    return this.asSequence()
        .filter { news ->
            try {
                val date = LocalDateTime.ofEpochSecond(news.publication_date, 0, ZoneOffset.UTC).toLocalDate()
                date in period
            } catch (e: Exception) {
                logger.error { "Error parsing date for news item: ${news.id}, ${e.message}" }
                false
            }
        }
        .map { news ->
            try {
                news
            } catch (e: Exception) {
                logger.error { "Error calculating rating for news item: ${news.favorites_count}, ${news.comments_count}, ${e.message}" }
                news
            }
        }
        .sortedByDescending { it.rating }
        .take(count)
        .toList()
}


