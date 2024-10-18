import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.example.ApiKudaGo
import org.example.News
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertFalse

class WorkerTest {

    private lateinit var newsChannel: Channel<List<News>>
    private lateinit var mockApi: ApiKudaGo

    @BeforeEach
    fun setup() {
        newsChannel = Channel()
        mockApi = Mockito.mock(ApiKudaGo::class.java)
    }

    @AfterEach
    fun tearDown() {
        newsChannel.close()
    }

    @Test
    fun `startWorker_SendsNewsToChannel_NewsIsReceivedCorrectly`() = runTest {
        // Arrange
        val mockNews = listOf(News(
            id = 1L,
            publication_date = 1697625600000L,
            title = "title",
            place = null,
            description = "description",
            site_url = "someURL",
            favorites_count = 1,
            comments_count = 1L
        ))

        Mockito.`when`(mockApi.getNews(Mockito.anyInt())).thenReturn(mockNews)

        // Act
        val worker = Worker(newsChannel, mockApi)
        val job = launch {
            worker.start(1, 5)
        }

        val result = newsChannel.receive()

        // Assert
        assertNotNull(result)
        assertFalse(result.isEmpty())
        assertEquals(mockNews, result)

        Mockito.verify(mockApi).getNews(1 % 5)

        job.join()
    }
}
