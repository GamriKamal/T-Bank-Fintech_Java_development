import kotlinx.coroutines.channels.Channel
import org.example.News
import java.io.File

class Processor(private val newsChannel: Channel<List<News>>) {

    suspend fun processToFile(filePath: String) {
        File(filePath).bufferedWriter().use { writer ->
            for (newsBatch in newsChannel) {
                for (news in newsBatch) {
                    writer.write(news.toString())
                    writer.newLine()
                }
            }
        }
        println("Processing completed. Data saved to $filePath")
    }
}
