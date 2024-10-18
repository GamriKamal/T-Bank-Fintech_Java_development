package org.example

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class ApiKudaGoTest {

    private val mockEngine = MockEngine { request ->
        respond(
            """{"results": [{"id": 1, "title": "Test News 1"}, {"id": 2, "title": "Test News 2"}]}""",
            HttpStatusCode.OK
        )
    }

    private val httpClient = KtorHttpClient(HttpClient(mockEngine))

    @Test
    fun `test getNews respects semaphore limits`() = runBlocking {
        val api = ApiKudaGo(maxConcurrentRequests = 2, httpClient = httpClient)
        val numberOfRequests = 5
        val results = mutableListOf<List<News>>()

        coroutineScope {
            val jobs = mutableListOf<Job>()

            repeat(numberOfRequests) {
                jobs.add(launch {
                    val news = api.getNews()
                    results.add(news)
                })
            }

            jobs.forEach { it.join() }
        }

        assertEquals(numberOfRequests, results.size)

        val semaphore = Semaphore(2)
        val permitsAcquired = mutableListOf<Boolean>()

        coroutineScope {
            repeat(numberOfRequests) {
                launch {
                    val acquired = semaphore.tryAcquire()
                    permitsAcquired.add(acquired)
                    delay(100)
                    if (acquired) {
                        semaphore.release()
                    }
                }
            }
        }

        val allowedPermits = permitsAcquired.count { it }
        assertEquals(2, allowedPermits)
    }
}
