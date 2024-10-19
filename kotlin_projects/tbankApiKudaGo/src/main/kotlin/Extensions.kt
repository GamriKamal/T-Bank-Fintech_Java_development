package org.example

import mu.KotlinLogging
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
    return try {
        val filteredNews = this.filter { news ->
            try {
                val date = LocalDateTime.ofEpochSecond(news.publication_date, 0, ZoneOffset.UTC).toLocalDate()
                date in period
            } catch (e: Exception) {
                logger.error { "Error parsing date for news item: ${news.id}, ${e.message}" }
                false
            }
        }

        val sortedNews = filteredNews.sortedByDescending { news ->
            try {
                news.rating
            } catch (e: Exception) {
                logger.error { "Error calculating rating for news item: ${news.favorites_count}, ${news.comments_count}, ${e.message}" }
                0.0
            }
        }

        sortedNews.take(count)
    } catch (e: Exception) {
        logger.error { "An error occurred while processing news: ${e.message}" }
        emptyList()
    }
}

fun List<News>.printNews(configure: Options.() -> Unit = {}) {
    logger.info { "\n" }
    val options = Options().apply(configure)

    val formattedItems = this.mapIndexed { index, news ->
        val formattedItem = """
            id: ${news.id}
            publication_date: ${news.publication_date}
            title: ${news.title}
            place: ${news.place}
            description: ${news.description}
            site_url: ${news.site_url}
            favorites_count: ${news.favorites_count}
            comments_count: ${news.comments_count}
            rating: ${news.rating}
        """.trimIndent()

        val color = when {
            options.coloredOutput -> shadesOfBlue[index % shadesOfBlue.size]
            else -> null
        }
        color?.let { "$it$formattedItem\u001B[0m" } ?: formattedItem
    }

    val formattedString = formattedItems.joinToString(
        separator = "\n\n",
        prefix = options.prefix,
        postfix = options.suffix
    )

    println(formattedString)
}


