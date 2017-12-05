package cn.moonlight.wechatcopy.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cn.moonlight.wechatcopy.R

/**
 * Created by songyifeng on 2017/12/4.
 */
class RefreshAdapter(datas: ArrayList<String>, context: Context) : RecyclerView.Adapter<RefreshAdapter.ViewHolder>() {
    val mContext: Context
    val mDatas: ArrayList<String>

    //Type
    private val TYPE_NORMAL = 1000
    private val TYPE_HEADER = 1001
    private val TYPE_FOOTER = 1002

    private var VIEW_FOOTER: View? = null
    private var VIEW_HEADER: View? = null

    init {
        mDatas = datas
        mContext = context
    }

    override fun getItemCount(): Int {
        var count = if (mDatas == null) 0 else mDatas.size
        if (VIEW_FOOTER != null)
            count++
        if (VIEW_HEADER != null)
            count++
        return count
    }

    override fun getItemViewType(position: Int): Int = when {
        isHeaderView(position) -> TYPE_HEADER
        isFooterView(position) -> TYPE_FOOTER
        else -> TYPE_NORMAL
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var index = position
        if (!isHeaderView(position) && !isFooterView(position)) {

            if (haveHeaderView()) index--
            var textView: TextView = holder.itemView.findViewById(R.id.textview)
            textView.setTextColor(Color.WHITE)
            textView.text = mDatas[index]

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            when (viewType) {
                TYPE_HEADER -> ViewHolder(VIEW_HEADER)
                TYPE_FOOTER -> ViewHolder(VIEW_FOOTER)
                else -> ViewHolder(getLayout(R.layout.item))
            }


    private fun getLayout(layoutId: Int): View = LayoutInflater.from(mContext).inflate(layoutId, null)

    fun addHeaderView(headerView: View) {
        if (haveHeaderView()) {
            throw IllegalStateException("hearview has already exists!")
        } else {
            //避免出现宽度自适应
            var params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            headerView.layoutParams = params
            VIEW_HEADER = headerView
            // ifGridLayoutManager()
            notifyItemInserted(0)
        }
    }

    fun addFooterView(footerView: View) {
        if (haveFooterView()) {
            throw IllegalStateException("footerView has already exists!");
        } else {
            var params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            footerView.layoutParams = params
            VIEW_FOOTER = footerView
//                ifGridLayoutManager()
            notifyItemInserted(itemCount - 1)
        }
    }


    private fun haveHeaderView(): Boolean = VIEW_HEADER != null


    private fun haveFooterView(): Boolean = VIEW_FOOTER != null


    private fun isHeaderView(position: Int): Boolean = haveHeaderView() && position == 0


    private fun isFooterView(position: Int): Boolean = haveFooterView() && position == itemCount - 1

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view)
}
