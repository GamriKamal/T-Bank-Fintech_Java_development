package org.example

suspend fun main() {
    val result: List<News> = ApiKudaGo().getNews(10)

    val startDate = ApiKudaGo().parseDate("13.09.2024")
    val endDate = ApiKudaGo().parseDate("14.09.2024")

    val period = startDate..endDate
    val mostRatedNews = result.getMostRatedNews(10, period)

    ApiKudaGo().saveNews("./src/main/resources/mostRatedNews.csv", mostRatedNews)

    mostRatedNews.printNews { coloredOutput = true }
}
