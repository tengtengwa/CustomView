package com.example.customview.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.customview.utils.dp2px
import kotlin.math.max

/**
 * 流式布局，优先横向摆放子View，当宽度超过parent宽度时，将其放到下一行行首，直到摆放完所有子View
 */
class FlowLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributes, defStyleAttr) {
    //每一行的子view列表
    private val lineViews: MutableList<LineView> = mutableListOf(LineView())
    //每列默认间隔
    var VERTICAL_SPACE = 5.dp2px(context)
    //每行默认间隔
    var HORIZONTAL_SPACE = 5.dp2px(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val availableWidth = widthSize - paddingLeft - paddingRight

        lineViews.clear()
        var curLineView = LineView()
        lineViews.add(curLineView)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                continue
            }

            val childWidthMode = if (widthMode==MeasureSpec.EXACTLY) MeasureSpec.AT_MOST else widthMode
            val childHeightMode = if (heightMode==MeasureSpec.EXACTLY) MeasureSpec.AT_MOST else heightMode

            child.measure(
                MeasureSpec.makeMeasureSpec(widthSize, childWidthMode),
                MeasureSpec.makeMeasureSpec(heightSize, childHeightMode)
            )
            val childHeight = child.measuredHeight
            val childWidth = child.measuredWidth

            //Children宽度和未超过parent，或当前行要放的第一个View直接超过parent宽度了，则直接放入当前行，否则放下一行
            if (curLineView.viewList.size != 0 &&
                curLineView.totalWidth + HORIZONTAL_SPACE + childWidth > availableWidth) {
                curLineView = LineView()
                lineViews.add(curLineView)
            }
            curLineView.apply {
                totalWidth += (HORIZONTAL_SPACE + childWidth)
                maxHeight = max(maxHeight, childHeight)
                viewList.add(child)
            }
        }
        var totalHeight = lineViews.sumOf { it.maxHeight}
        totalHeight += (VERTICAL_SPACE * (lineViews.size - 1))
        totalHeight += paddingTop + paddingBottom
        setMeasuredDimension(widthSize, resolveSize(totalHeight, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val left = paddingLeft
        var curTop = paddingTop
        lineViews.forEachIndexed { i, it ->
            it.layoutChildren(left, curTop)
            curTop += if (i == 0) 0 else VERTICAL_SPACE
            curTop += it.maxHeight
        }
    }

    private inner class LineView {
        //当前行总宽度
        var totalWidth = 0
        //当前行总高度
        var maxHeight = 0
        //每一行View的列表
        val viewList = mutableListOf<View>()

        fun layoutChildren(left: Int, top: Int) {
            var curLeft = left
            viewList.forEachIndexed { i, it ->
                it.layout(curLeft, top, curLeft + it.measuredWidth, top + it.measuredHeight)
                curLeft += if (i==0) 0 else HORIZONTAL_SPACE
                curLeft += it.measuredWidth
            }
        }
    }
}