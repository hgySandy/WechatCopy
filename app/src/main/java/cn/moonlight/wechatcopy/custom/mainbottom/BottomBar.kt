package cn.moonlight.wechatcopy.custom.mainbottom

import android.content.Context
import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.LinearLayout
import java.util.*

/**
 * Created by songyifeng on 2017/12/5.
 */
class BottomBar(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr) {

    private val TRANSLATE_DURATION_MILLIS = 200

    private val mInterpolator = AccelerateDecelerateInterpolator()
    private var mVisible = true

    private val mTabs = ArrayList<BottomBarTab>()

    private var mTabLayout: LinearLayout
    private var mTabParams: LinearLayout.LayoutParams

    private var mCurrentPosition = 0
    private var mListener: OnTabSelectedListener? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        orientation = LinearLayout.VERTICAL

        //        ImageView shadowView = new ImageView(context);
        //        shadowView.setBackgroundResource(R.drawable.actionbar_shadow_up);
        //        addView(shadowView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mTabLayout = LinearLayout(context)
        mTabLayout.setBackgroundColor(Color.WHITE)
        mTabLayout.orientation = LinearLayout.HORIZONTAL
        addView(mTabLayout, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        mTabParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        mTabParams.weight = 1f
    }

    fun addItem(tab: BottomBarTab): BottomBar {
        tab.setOnClickListener {
            mListener?.let {
                val pos = tab.tabPosition
                if (mCurrentPosition == pos) {
                    it.onTabReselected(pos)
                } else {
                    it.onTabSelected(pos, mCurrentPosition)
                    tab.setSelected(true)
                    it.onTabUnselected(mCurrentPosition)
                    mTabs[mCurrentPosition].setSelected(false)
                    mCurrentPosition = pos
                }
            }
        }
        tab.tabPosition = mTabLayout.childCount
        tab.layoutParams = mTabParams
        mTabLayout.addView(tab)
        mTabs.add(tab)
        return this
    }

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener) {
        mListener = onTabSelectedListener
    }

    fun setCurrentItem(position: Int) {
        mTabLayout.post(Runnable { mTabLayout.getChildAt(position).performClick() })
    }

    fun getCurrentItemPosition(): Int {
        return mCurrentPosition
    }

    /**
     * 获取 Tab
     */
    fun getItem(index: Int): BottomBarTab? {
        return if (mTabs.size < index) null else mTabs[index]
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, prePosition: Int)

        fun onTabUnselected(position: Int)

        fun onTabReselected(position: Int)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, mCurrentPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)

        if (mCurrentPosition != ss.position) {
            mTabLayout.getChildAt(mCurrentPosition).setSelected(false)
            mTabLayout.getChildAt(ss.position).setSelected(true)
        }
        mCurrentPosition = ss.position
    }

    internal class SavedState : View.BaseSavedState {
        var position: Int = 0

        constructor(source: Parcel) : super(source) {
            position = source.readInt()
        }

        constructor(superState: Parcelable, position: Int) : super(superState) {
            this.position = position
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }


    fun hide() {
        hide(true)
    }

    fun show() {
        show(true)
    }

    fun hide(anim: Boolean) {
        toggle(false, anim, false)
    }

    fun show(anim: Boolean) {
        toggle(true, anim, false)
    }

    fun isVisible(): Boolean {
        return mVisible
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (mVisible != visible || force) {
            mVisible = visible
            val height = height
            if (height == 0 && !force) {
                val vto = viewTreeObserver
                if (vto.isAlive) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = viewTreeObserver
                            if (currentVto.isAlive) {
                                currentVto.removeOnPreDrawListener(this)
                            }
                            toggle(visible, animate, true)
                            return true
                        }
                    })
                    return
                }
            }
            val translationY = if (visible) 0 else height
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                        .translationY(translationY.toFloat())
            } else {
                ViewCompat.setTranslationY(this, translationY.toFloat())
            }
        }
    }
}