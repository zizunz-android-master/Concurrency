package happy.mjstudio.concurrencysample.model

import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object VideoDownloadService {

    fun downloadVideo() : Video {
        Thread.sleep(createDelay())
        return createVideo()
    }

    fun downloadVideoSingle() : Single<Video> {
        return Single.just(createVideo()).delay(createDelay(),TimeUnit.MILLISECONDS)
    }

    suspend fun downloadVideoCoroutine() : Video = withContext(Dispatchers.IO) {
        delay(createDelay())
        createVideo()
    }


    private fun createDelay() : Long = 5000L

    private fun createVideo() : Video {
        val videoName = "Video-${Random.nextInt(1000)}"
        val videoSize = Random.nextInt(10000,100000)

        return Video(videoName,videoSize)
    }
}