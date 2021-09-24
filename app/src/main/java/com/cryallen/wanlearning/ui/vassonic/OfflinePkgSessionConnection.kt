package com.cryallen.wanlearning.ui.vassonic

import android.content.Context
import android.content.Intent
import com.tencent.sonic.sdk.SonicConstants
import com.tencent.sonic.sdk.SonicSession
import com.tencent.sonic.sdk.SonicSessionConnection
import java.io.BufferedInputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.*

class OfflinePkgSessionConnection(context: Context, session: SonicSession?, intent: Intent?) : SonicSessionConnection(session, intent) {

    private val context: WeakReference<Context>

    override fun internalConnect(): Int {
        val ctx = context.get()
        if (null != ctx) {
            try {
                val offlineHtmlInputStream = ctx.assets.open("sonic-demo-index.html")
                responseStream = BufferedInputStream(offlineHtmlInputStream)
                return SonicConstants.ERROR_CODE_SUCCESS
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
        return SonicConstants.ERROR_CODE_UNKNOWN
    }

    override fun internalGetResponseStream(): BufferedInputStream {
        return responseStream
    }

    override fun internalGetCustomHeadFieldEtag(): String {
        return CUSTOM_HEAD_FILED_ETAG
    }

    override fun disconnect() {
        if (null != responseStream) {
            try {
                responseStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getResponseCode(): Int {
        return 200
    }

    override fun getResponseHeaderFields(): Map<String, List<String>> {
        return HashMap(0)
    }

    override fun getResponseHeaderField(key: String?): String {
        return ""
    }

    init {
        this.context = WeakReference(context)
    }
}