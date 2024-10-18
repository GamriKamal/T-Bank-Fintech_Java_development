import kotlinx.coroutines.channels.Channel
import org.example.ApiKudaGo
import org.example.News

class Worker(private val newsChannel: Channel<List<News>>, private val apiKudaGo: ApiKudaGo) {

    suspend fun start(workerId: Int, newsCount: Int) {
        println("Worker #$workerId started")
        val pagesToFetch = workerId % newsCount
        val result: List<News> = apiKudaGo.getNews(pagesToFetch)
        newsChannel.send(result)
        println("Worker #$workerId finished")
    }
}

