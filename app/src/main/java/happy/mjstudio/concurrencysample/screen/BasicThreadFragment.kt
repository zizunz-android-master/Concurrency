package happy.mjstudio.concurrencysample.screen

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import happy.mjstudio.concurrencysample.R
import happy.mjstudio.concurrencysample.model.VideoDownloadService
import kotlinx.android.synthetic.main.fragment_basic_thread.*

class BasicThreadFragment : Fragment(), VideoDownloader {

    lateinit var popupMenu: androidx.appcompat.widget.PopupMenu

    private lateinit var thread : Thread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_basic_thread, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initPopupMenu()
        menu.setOnClickListener {
            popupMenu.show()
        }
    }

    private fun initPopupMenu() {
        popupMenu = androidx.appcompat.widget.PopupMenu(context!!, menu, Gravity.RIGHT or Gravity.BOTTOM).apply {
            menuInflater.inflate(R.menu.menu, this.menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_download -> {
                        download()
                    }
                    R.id.menu_cancel -> {
                        cancel()
                    }
                    R.id.menu_reset -> {
                        reset()
                    }
                }

                true
            }
        }
    }

    override fun download() {

        thread = kotlin.concurrent.thread {
            val video = VideoDownloadService.downloadVideo()

            activity?.runOnUiThread {
                textView.text = video.toString()
            }
        }
        ObjectAnimator.ofInt(progressBar,"progress",0,10000).apply {
            duration = 5000L
            setAutoCancel(true)
            start()
        }
        textView.text = "Downloading..."
    }

    override fun cancel() {
        //No way to cancel Thread ( thread.interrupt() is not matched our use case )
        ObjectAnimator.ofInt(progressBar,"progress",0,progressBar.progress).apply {
            duration = 0L
            setAutoCancel(true)
            start()
        }
        textView.text = "Cancelled"
    }

    override fun reset() {
        //No way to cancel Thread ( thread.interrupt() is not matched our use case )
        ObjectAnimator.ofInt(progressBar,"progress",0).apply {
            duration = 1000L
            start()
        }
        textView.text = "Waiting..."
    }

}

