package cn.moonlight.wechatcopy.fragment.second

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.moonlight.wechatcopy.R
import cn.moonlight.wechatcopy.custom.momentsrefresh.MyRefreshLayout
import cn.moonlight.wechatcopy.fragment.BaseFragment

/**
 * Created by songyifeng on 2017/12/4.
 */
class ContactsFragment : BaseFragment() {

    lateinit var recycleview:RecyclerView
    lateinit var refreshlayout: MyRefreshLayout
    lateinit var mLinearLayoutManager: LinearLayoutManager

    companion object {
        @JvmStatic
        fun newInstance(): ContactsFragment {
            val args = Bundle()
            val fragment = ContactsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_contacts,container,false)
        initView(view)
        setListener()
        initData()
        return view
    }

    private fun initView(view:View){

    }
    private fun setListener(){

    }
    private fun initData() {

    }
}