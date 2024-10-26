package org.example

import Processor
import Worker
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Semaphore
import kotlin.system.measureTimeMillis

val dispatcher = newFixedThreadPoolContext(4, "WorkerPool")

suspend fun main() = coroutineScope {
    val newsChannel = Channel<List<News>>()
    val workerCount = 2
    val maxConcurrentRequests = 5
    val apiKudaGo = ApiKudaGo(maxConcurrentRequests)

    val workerJobs = mutableListOf<Job>()

    val executionTime = measureTimeMillis {
        repeat(workerCount) { index ->
            val job = launch(dispatcher) {
                Worker(newsChannel, apiKudaGo).start(index % workerCount, 90)
            }
            workerJobs.add(job)
        }

        val processor = Processor(newsChannel)
        val processorJob = launch {
            processor.processToFile("./src/main/resources/mostRatedNews.csv")
        }

        workerJobs.joinAll()
        newsChannel.close()
        processorJob.join()
    }

    println("Execution completed in $executionTime ms")
}