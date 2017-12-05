package cn.moonlight.wechatcopy

import android.os.Bundle
import cn.moonlight.wechatcopy.fragment.MainFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(findFragment(MainFragment::class.java) == null)
            loadRootFragment(R.id.fl_container,MainFragment.newInstance())

    }
}
