package org.example

import com.fintech.kotlin.dsl.prettyPrint
import com.fintech.kotlin.dsl.prettyPrintHTML

suspend fun main() {
    val result: List<News> = ApiKudaGo().getNews(90)

    val startDate = ApiKudaGo().parseDate("08.11.2024")
    val endDate = ApiKudaGo().parseDate("24.11.2024")

    val period = startDate..endDate
    val mostRatedNews = result.getMostRatedNews(30, period)

    ApiKudaGo().saveNews("./src/main/resources/mostRatedNews.csv", mostRatedNews)

    println(prettyPrint(mostRatedNews))
    println()
    println(prettyPrintHTML(mostRatedNews))
}

