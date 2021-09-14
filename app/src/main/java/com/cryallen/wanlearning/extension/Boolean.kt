/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.view.View

/**
 * Boolean转Visibility
 */
fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE