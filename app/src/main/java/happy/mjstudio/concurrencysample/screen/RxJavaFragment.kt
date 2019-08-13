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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_rxjava.*

class RxJavaFragment : Fragment(),VideoDownloader {

    lateinit var popupMenu: androidx.appcompat.widget.PopupMenu

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_rxjava,container,false)
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
        compositeDisposable += VideoDownloadService.downloadVideoSingle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({video->
                textView.text = video.toString()
            }, {

            })

        ObjectAnimator.ofInt(progressBar,"progress",0,10000).apply {
            duration = 5000L
            setAutoCancel(true)
            start()
        }

        textView.text = "Downloading..."
    }

    override fun cancel() {
        compositeDisposable.clear()
        ObjectAnimator.ofInt(progressBar,"progress",0,progressBar.progress).apply {
            duration = 0L
            setAutoCancel(true)
            start()
        }
        textView.text = "Cancelled"
    }

    override fun reset() {
        compositeDisposable.clear()
        ObjectAnimator.ofInt(progressBar,"progress",0).apply {
            duration = 1000L
            start()
        }
        textView.text = "Waiting..."
    }
}