package happy.mjstudio.concurrencysample.screen

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.mjstudio.concurrencysample.R
import kotlinx.android.synthetic.main.fragment_basic_thread.*
import kotlin.concurrent.thread

class BasicThreadFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic_thread, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        /**
         * 동기화 코드를 이용한 비디오 다운로드 및 UI에 표시하기
         */
        syncDownButton.setOnClickListener {
            val video = downloadVideoSync()
            syncDownResultTextView.text = video.toString()
        }

        /**
         * 비동기화 코드와 콜백 패턴을 이용한 비디오 다운로드 및 UI에 표시하기
         *
         * Main Thread에서만 UI작업을 해줄 수 있기 때문에 코드가 많이 더러워진다.
         *
         *
         */
        asyncDownButton.setOnClickListener {
            downloadVideoAsync { video ->
                activity?.runOnUiThread {
                    asyncDownResultTextView.text = video.toString()
                }
            }
        }


        val thread = HandlerThread("My Handler Thread")
        thread.start()

        val handler = Handler(thread.looper) //Error

        handler.post {
            //이 코드는 우리의 MyThreadWithLooper 객체의 쓰레드에서 실행됩니다.
        }

    }


    data class Video(
        val name: String,
        val size: Int
    ) {
        override fun toString() = "내 비디오의 이름 : $name 크기 : $size"
    }

    private fun downloadVideoSync(): Video {
        Thread.sleep(5000)
        val video = Video("Sync Video", 5000)
        return video
    }

    private fun downloadVideoAsync(callback: (Video) -> Unit) {
        thread {
            Thread.sleep(5000)
            val video = Video("Async Video", 5000)
            callback(video)
        }
    }
}

class myThreadWithoutLooper : Thread() {
    override fun run() {

    }
}
