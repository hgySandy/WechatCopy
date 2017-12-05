package cn.moonlight.wechatcopy.fragment.third

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.moonlight.wechatcopy.R
import cn.moonlight.wechatcopy.adapter.RefreshAdapter
import cn.moonlight.wechatcopy.custom.momentsrefresh.CustomProgressDrawable
import cn.moonlight.wechatcopy.custom.momentsrefresh.MyRefreshLayout
import cn.moonlight.wechatcopy.fragment.BaseFragment

/**
 * Created by songyifeng on 2017/12/4.
 */
class MomentsFragment: BaseFragment() {

    lateinit var recycleview:RecyclerView
    lateinit var refreshlayout: MyRefreshLayout
    lateinit var mLinearLayoutManager: LinearLayoutManager

    companion object {
        @JvmStatic
        fun newInstance(): MomentsFragment {
            val args = Bundle()
            val fragment = MomentsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_moments,container,false)
        initView(view)
        setListener()
        initData()
        return view
    }

    private fun initView(view:View){
        mLinearLayoutManager = LinearLayoutManager(activity)
        recycleview = view.findViewById(R.id.recycleview)
        refreshlayout = view.findViewById(R.id.refreshlayout)
        recycleview.layoutManager = mLinearLayoutManager
        recycleview.setHasFixedSize(true)
    }
    private fun setListener(){
        var drawable = CustomProgressDrawable(activity, refreshlayout)
        var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.icon_moments)
        drawable.setBitmap(bitmap)
        refreshlayout.setProgressView(drawable)
        refreshlayout.setBackgroundColor(Color.BLACK)
        refreshlayout.setProgressBackgroundColorSchemeColor(Color.TRANSPARENT)
        refreshlayout.setOnRefreshListener {
            Handler().postDelayed({
                if (refreshlayout.isRefreshing)
                    refreshlayout.isRefreshing = false
            }, 2000)
        }
    }
    private fun initData() {
        val list = arrayListOf("aaa","bbb","ccc","ddd")
        val biglist = ArrayList<String>()
        for (i in 1..10){
            biglist.addAll(list)
        }
        var adapter = RefreshAdapter(biglist, activity)
        var header = LayoutInflater.from(activity).inflate(R.layout.item_header_layout,null)
        adapter.addHeaderView(header)
//        adapter.addFooterView(header)
        recycleview.adapter = adapter
    }
}