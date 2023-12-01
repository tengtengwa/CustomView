package com.example.customview.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.Scroller
import kotlin.math.max

/**
 * 仿QQ Feed列表item样式的DrawableLayout
 *
 * 主要实现思路：
 * - DrawableLayout作为一个容器，只需要有两个子View，一个contentView，一个menuView（默认隐藏），左滑可以展示menuView
 * - onLayout方法中，将menuView放置到
 */
class DrawableLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defaultAttr: Int = 0
) : LinearLayout(context, attributeSet, defaultAttr) {

    private val TAG = "DrawableLayout"

    private var contentWidth = 0
    private var menuWidth = 0
    private var maxHeight = 0
    private lateinit var vContent: View
    private lateinit var vMenu: View
    private val scroller = Scroller(context)

    //记录上次pointer的X位置，注意不要定义在onTouchEvent中
    private var lastX = 0f
    private var startX = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        val newX = event.x
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //因为DOWN事件需要交给子View进行处理，所以需要在onInterceptTouchEvent方法中获取DOWN事件的X坐标
            }

            MotionEvent.ACTION_UP -> {
                //抬起时通过判断偏移量scrollX是否大于menuWidth/2，来判断打开/关闭菜单
                if (scrollX > menuWidth / 2) {
                    changeMenuState(true)
                } else {
                    changeMenuState(false)
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = newX - lastX
//                Log.d(TAG, "deltaX：$deltaX，lastX：$lastX，newX：$newX")
                performHorizontalScroll(deltaX)
                lastX = newX
            }
        }
        return true
    }


    /**
     * DrawableLayout作为一个容器，仅当滑动时才拦截事件，否则交给子View处理点击事件
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var intercept = false
        val newX = ev.x
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = newX
                lastX = newX
                Log.d(TAG, "DOWN   newX:$newX, lastX:$lastX")
            }

            MotionEvent.ACTION_MOVE -> {
                intercept = true
            }
        }
        return intercept
    }

    /**
     * @param deltaX 本次要滑动的距离，左滑负，右滑正
     */
    private fun performHorizontalScroll(deltaX: Float) {
        //scrollX是左边界相对于原始位置左边界的偏移量，左为正，右为负
        var dX = (scrollX - deltaX).toInt()    //本次滑动的偏移量=上次滑动后的偏移量-本次滑动的距离
        Log.d(TAG, "scrollX:$scrollX, deltaX：$deltaX，lastX：$lastX")
        //非法判断，滑动偏移量不能超过[0,menuWidth]
        if (dX < 0) {
            dX = 0
        }
        if (dX > menuWidth) {
            dX = menuWidth
        }
        scrollTo(dX, scrollY)
    }

    private fun changeMenuState(openMenu: Boolean) {
        val dX = if (openMenu) menuWidth - scrollX else -scrollX
        scroller.startScroll(scrollX, scrollY, dX, 0)
        invalidate()
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        vContent = getChildAt(0)
        vMenu = getChildAt(1)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //content默认占满父View
        vContent.layoutParams.apply {
            width = LayoutParams.MATCH_PARENT
        }
        contentWidth = vContent.measuredWidth
        menuWidth = vMenu.measuredWidth
        maxHeight = max(vContent.measuredHeight, vMenu.measuredHeight)
        Log.d(TAG, "contentWidth：$contentWidth，menuWidth：$menuWidth，maxHeight：$maxHeight")
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
//        vContent.layout(0, 0, contentWidth, maxHeight)
//        vMenu.layout(contentWidth, 0, contentWidth + menuWidth, maxHeight)
    }
}