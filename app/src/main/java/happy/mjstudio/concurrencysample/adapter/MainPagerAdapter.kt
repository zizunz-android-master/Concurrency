package happy.mjstudio.concurrencysample.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import happy.mjstudio.concurrencysample.screen.BasicThreadFragment
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class MainPagerAdapter (activity : FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount() = NavigationMenu.getAllNavigationMenu().size
    override fun createFragment(position: Int) = NavigationMenu.getFragmentWithIndex(position)

    /**
     * 각각 네비게이션 메뉴를 나타내기 위한 enum 클래스
     *
     * @property kClass 는 코틀린 클래스로써 각 프라그먼트들의 클래스를 의미한다.
     */
    enum class NavigationMenu(val kClass: KClass<out Fragment>) {
        BASIC_THREAD(BasicThreadFragment::class),
        BASIC_THREAD2(BasicThreadFragment::class),
        BASIC_THREAD3(BasicThreadFragment::class),
        BASIC_THREAD4(BasicThreadFragment::class),

        ;

        companion object {
            /**
             * 모든 네비게이션 메뉴 아이템을 반환한다.
             *
             * @return 모든 네비게이션 메뉴 아이템들
             */
            fun getAllNavigationMenu() = NavigationMenu.values()

            /**
             * 프라그먼트 객체를 생성해서 인자로 들어온 Argument 들까지 추가해서 반환해준다.
             *
             * 이 과정에서 우리가 enum 클래스의 속성으로 갖고있는 kClass 가 쓰인다.
             *
             * [KClass] 클래스에서 주 생성자(primaryConstructor)를 얻어오기 위해서는 다음과 같은 의존성 추가가 필요하다.
             *
             * implementation "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
             *
             * @param index 네비게이션 메뉴의 인덱스
             * @param args 프라그먼트의 [arguments] 로 전달해 줄 [Bundle] 객체
             *
             * @return [Fragment] 객체
             */
            fun getFragmentWithIndex(index: Int, args: Bundle = Bundle()): Fragment {
                val kClass = getAllNavigationMenu()[index].kClass
                val frag = kClass.primaryConstructor?.call() ?: throw IllegalArgumentException("No matching Fragment primary constructor")

                //Add Argument
                with(frag.arguments) {
                    this?.putAll(args)
                }

                return frag
            }
        }
    }
}