package cn.moonlight.wechatcopy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.moonlight.wechatcopy.R
import cn.moonlight.wechatcopy.custom.mainbottom.BottomBar
import cn.moonlight.wechatcopy.custom.mainbottom.BottomBarTab
import cn.moonlight.wechatcopy.event.EventBusActivityScope
import cn.moonlight.wechatcopy.event.TabSelectedEvent
import cn.moonlight.wechatcopy.fragment.first.MsgFragment
import cn.moonlight.wechatcopy.fragment.forth.MineFragment
import cn.moonlight.wechatcopy.fragment.second.ContactsFragment
import cn.moonlight.wechatcopy.fragment.third.MomentsFragment
import me.yokeyword.fragmentation.SupportFragment

/**
 * Created by songyifeng on 2017/11/28.
 */
class MainFragment:BaseFragment() {

    private lateinit var mBottomBar: BottomBar

    companion object {
        @JvmField
        val FIRST = 0
        @JvmField
        val SECOND = 1
        @JvmField
        val THIRD = 2
        @JvmField
        val FORTH = 3

        @JvmStatic
        fun newInstance():MainFragment{
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val mFragments = arrayOfNulls<SupportFragment>(4)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_main,container,false)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val firstFragment = findChildFragment(MsgFragment::class.java)
        if(firstFragment == null){
            mFragments[FIRST] = MsgFragment.newInstance()
            mFragments[SECOND] = ContactsFragment.newInstance()
            mFragments[THIRD] = MomentsFragment.newInstance()
            mFragments[FORTH] = MineFragment.newInstance()
            loadMultipleRootFragment(R.id.fl_tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FORTH])
        }else{
            mFragments[FIRST] = firstFragment
            mFragments[SECOND] = findChildFragment(ContactsFragment::class.java)
            mFragments[THIRD] = findChildFragment(MomentsFragment::class.java)
            mFragments[FORTH] = findChildFragment(MineFragment::class.java)
        }
    }

    fun initView(view:View){
        mBottomBar = view.findViewById(R.id.bottomBar)

        mBottomBar
                .addItem(BottomBarTab(_mActivity, R.drawable.ic_message_white_24dp, "消息"))
                .addItem(BottomBarTab(_mActivity, R.drawable.ic_contacts_white_24dp, "通讯录"))
                .addItem(BottomBarTab(_mActivity, R.drawable.ic_discover_white_24dp, "发现"))
                .addItem(BottomBarTab(_mActivity, R.drawable.ic_account_circle_white_24dp, "我的"))

        // 模拟未读消息
        mBottomBar.getItem(FIRST)?.unreadCount = 9

        mBottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener{
            override fun onTabSelected(position: Int, prePosition: Int) {
                showHideFragment(mFragments[position], mFragments[prePosition])

                val tab = mBottomBar.getItem(FIRST)
                tab?.let {
                    if (position == FIRST) {
                        it.unreadCount = 0
                    } else {
                        it.unreadCount = it.unreadCount + 1
                    }
                }

            }

            override fun onTabUnselected(position: Int) {

            }

            override fun onTabReselected(position: Int) {
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBusActivityScope.getDefault(_mActivity).post(TabSelectedEvent(position))
            }
        })
    }

}