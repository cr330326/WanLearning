package com.cryallen.wanlearning.constant

import androidx.annotation.StringDef

@StringDef(MMKVKey.TEST)
@Retention(AnnotationRetention.SOURCE)
annotation class MMKVKey {
    companion object {
        const val TEST = "test"
    }
}
