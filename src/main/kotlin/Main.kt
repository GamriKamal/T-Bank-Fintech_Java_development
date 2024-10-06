package org.example

suspend fun main() {
    val result: List<News> = ApiKudaGo().getNews(90)

    val startDate = ApiKudaGo().parseDate("10.09.2024")
    val endDate = ApiKudaGo().parseDate("24.09.2024")

    val period = startDate..endDate
    val mostRatedNews = result.getMostRatedNews(30, period)

    ApiKudaGo().saveNews("./src/main/resources/mostRatedNews.csv", mostRatedNews)

    mostRatedNews.printNews { coloredOutput = true }
}

