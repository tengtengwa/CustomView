package com.example.customview.utils

import android.content.Context

fun Int.dp2px(context: Context): Int {
    // 获取当前手机的像素密度（1个dp对应几个px）
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun Int.px2dp(context: Context): Int {
    // 获取当前手机的像素密度（1个dp对应几个px）
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}