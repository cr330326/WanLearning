/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.text.Html
import android.text.Spanned
import android.widget.Toast
import com.cryallen.wanlearning.WanApplication
import com.cryallen.wanlearning.ui.vassonic.SonicRuntimeImpl
import com.cryallen.wanlearning.utils.LogUtils
import com.tencent.sonic.sdk.SonicConfig
import com.tencent.sonic.sdk.SonicEngine
import com.tencent.sonic.sdk.SonicSessionConfig

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

/**
 * VasSonic预加载session。
 *
 * @param CharSequence 预加载url
 */
fun CharSequence.preCreateSession(): Boolean {
    if (!SonicEngine.isGetInstanceAllowed()) {
        SonicEngine.createInstance(SonicRuntimeImpl(WanApplication.instance), SonicConfig.Builder().build())
    }
    val sessionConfigBuilder = SonicSessionConfig.Builder().apply { setSupportLocalServer(true) }
    val preloadSuccess = SonicEngine.getInstance().preCreateSession(this.toString(), sessionConfigBuilder.build())
    LogUtils.d("preCreateSession()", "${this}\t:${if (preloadSuccess) "Preload start up success!" else "Preload start up fail!"}")
    return preloadSuccess
}