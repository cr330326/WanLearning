/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.text.Html
import android.text.Spanned
import android.widget.Toast
import com.cryallen.wanlearning.WanApplication

/**
 * 弹出Toast提示。
 *
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(WanApplication.instance, this, duration).show()
}

fun String.toHtml(flag: Int = Html.FROM_HTML_MODE_LEGACY): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, flag)
    } else {
        Html.fromHtml(this)
    }
}