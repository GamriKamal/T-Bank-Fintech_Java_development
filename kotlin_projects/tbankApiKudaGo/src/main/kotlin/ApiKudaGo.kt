package org.example

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.google.gson.stream.JsonReader
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import mu.KotlinLogging
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.IOException
import java.io.StringReader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface HttpClientInterface {
    suspend fun get(url: String): HttpResponse
}

class KtorHttpClient(private val client: HttpClient) : HttpClientInterface {
    override suspend fun get(url: String): HttpResponse {
        return client.get(url)
    }
}

class ApiKudaGo(
    private val maxConcurrentRequests: Int = 5,
    private val httpClient: HttpClientInterface = KtorHttpClient(HttpClient(CIO))
) {
    private val logger = KotlinLogging.logger {}
    private val semaphore = Semaphore(maxConcurrentRequests)

    suspend fun getNews(count: Int = 100): List<News> {
        val gson = Gson()
        val result = ArrayList<News>()

        semaphore.withPermit {
            try {
                val url = "https://kudago.com/public-api/v1.4/news/?lang=ru&page_size=$count&location=spb&fields=id%2Cpublication_date%2Ctitle%2Cplace%2Cdescription%2Csite_url%2Cfavorites_count%2Ccomments_count&order_by=-publication_date"
                val response: HttpResponse = httpClient.get(url)

                val jsonReader = JsonReader(StringReader(response.bodyAsText()))
                jsonReader.isLenient = true

                val jsonObject = JsonParser().parse(jsonReader).asJsonObject
                val resultsJsonArray = jsonObject.getAsJsonArray("results")
                for (item in resultsJsonArray) {
                    val temp = gson.fromJson(item, News::class.java)
                    result.add(temp)
                }

            } catch (e: ClientRequestException) {
                println("Error occurred: ${e.message}")
            } catch (e: JsonParseException) {
                println("JSON parse error: ${e.message}")
            } catch (e: Exception) {
                println("Exception: ${e.message}")
            }
        }
        return result
    }

    fun saveNews(path: String, news: Collection<News>) {
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
            logger.error { "Error occurred: ${e.stackTraceToString()}" }
        } catch (e: Exception) {
            logger.error { "Exception: ${e.message}" }
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
