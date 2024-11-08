package org.example

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.typeOf

class ApiKudaGo {
    private val client = HttpClient(CIO)
    private val logger = KotlinLogging.logger {}

    suspend fun getNews(count: Int = 100): List<News> {
        val gson = Gson()
        val result = ArrayList<News>()

        try {
            val response: HttpResponse =
                client.get("https://kudago.com/public-api/v1.4/news/?lang=ru&page_size=$count&location=spb&fields=id%2Cpublication_date%2Ctitle%2Cplace%2Cdescription%2Csite_url%2Cfavorites_count%2Ccomments_count&order_by=-publication_date")

            logger.debug { "Retrieved response: $response" }
            val jsonObject = JsonParser().parse(response.bodyAsText()).asJsonObject
            val resultsJsonArray = jsonObject.getAsJsonArray("results")

            for (item in resultsJsonArray) {
                var temp = gson.fromJson(item, News::class.java)
                result.add(temp)
            }
        } catch (e: ClientRequestException) {
            logger.error { "Error occurred: ${e.message}" }
        } catch (e: JsonParseException) {
            logger.error { "JSON parse error: ${e.message}" }
        } catch (e: Exception) {
            logger.error { "Exception: ${e.message}" }
        } finally {
            try {
                client.close()
                logger.info { "HttpClient has been closed successfully." }
            } catch (e: Exception) {
                logger.error { "Error closing HttpClient: ${e.message}" }
            }
        }
        return result
    }

    fun saveNews(path: String, news: Collection<News>) {
        val writer = BufferedWriter(Files.newBufferedWriter(Paths.get(path)))
        val csvPrinter = CSVPrinter(writer, CSVFormat.DEFAULT)

        try {
            val validatedPath = validateFilePath(path)

            BufferedWriter(Files.newBufferedWriter(validatedPath)).use { writer ->
                CSVPrinter(writer, CSVFormat.DEFAULT).use { csvPrinter ->
                    news.forEach { newsItem ->
                        try {
                            csvPrinter.printRecord(newsItem.toString())
                        } catch (e: IOException) {
                            logger.error { "Failed to write record for news ID ${newsItem.id}: ${e.message}" }
                        }
                    }
                }
            }

            logger.info { "Successfully created CSV file: $validatedPath" }
        } catch (e: IOException) {
            logger.error { "Error occurred while writing file: ${e.message}" }
        } catch (e: Exception) {
            logger.error { "Exception: ${e.message}" }
        } finally {
            try {
                csvPrinter.close()
                writer.close()
            } catch (e: Exception) {
                logger.error { "Failed to close resources: ${e.message}" }
            }
        }
    }

    private fun validateFilePath(path: String): Path {
        var filePath = Paths.get(path)

        if (Files.exists(filePath)) {
            val baseName = filePath.fileName.toString().substringBeforeLast(".")
            val extension = filePath.fileName.toString().substringAfterLast(".", "")
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))

            val newFileName = if (extension.isNotEmpty()) {
                "$baseName$timestamp.$extension"
            } else {
                "$baseName$timestamp"
            }

            filePath = filePath.resolveSibling(newFileName)
        }

        return filePath
    }

    fun parseDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}


