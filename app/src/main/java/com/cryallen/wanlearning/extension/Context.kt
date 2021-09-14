/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Context转Activity
 */
fun Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> {
            this
        }
        is ContextWrapper -> {
            this.baseContext.getActivity()
        }
        else -> null
    }
}
