/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.cryallen.wanlearning.WanApplication
import com.cryallen.wanlearning.utils.GlobalUtil

/**
 * 获取SharedPreferences实例。
 */
val sharedPreferences: SharedPreferences = WanApplication.instance.getSharedPreferences(GlobalUtil.appPackage + "_preferences", Context.MODE_PRIVATE)

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param block 处理点击事件回调代码块
 */
fun setOnClickListener(vararg v: View?, block: View.() -> Unit) {
    val listener = View.OnClickListener { it.block() }
    v.forEach { it?.setOnClickListener(listener) }
}

/**
 * 批量设置控件点击事件。
 *
 * @param v 点击的控件
 * @param listener 处理点击事件监听器
 */
fun setOnClickListener(vararg v: View?, listener: View.OnClickListener) {
    v.forEach { it?.setOnClickListener(listener) }
}


