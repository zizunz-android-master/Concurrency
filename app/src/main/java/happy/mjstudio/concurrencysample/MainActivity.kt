package happy.mjstudio.concurrencysample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.math.MathUtils
import androidx.viewpager2.widget.ViewPager2
import happy.mjstudio.concurrencysample.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager2()
    }

    /**
     * ViewPager2는 ViewPager보다 사용법이 간단하고 강력한 기능이 있다.
     *
     * 내부적으로 RecyclerView 를 사용한 것이기 때문에 RecyclerView 를 사용할 줄 안다면 어려움 없이 익힐 수 있다.
     */
    private fun initViewPager2() {
        with(viewPager2) {

            adapter = MainPagerAdapter(this@MainActivity)

            setPageTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
//                    page.alpha = 1- abs(position)
                    page.rotationY = position * -30
//                    page.translationY = abs(position) * 500
                    page.scaleX = MathUtils.clamp(1.5f - abs(position),0f,1f)
                    page.scaleY = MathUtils.clamp(1.5f - abs(position),0f,1f)
                }
            })
        }
    }


}
