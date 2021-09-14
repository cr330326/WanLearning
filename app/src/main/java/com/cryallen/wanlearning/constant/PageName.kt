package com.cryallen.wanlearning.constant

import androidx.annotation.StringDef


/***
 * 获取页面名称
 * @author vsh9p8q
 * @DATE 2021/9/12
 ***/
@StringDef(PageName.MAIN, PageName.HOME, PageName.MINE)
@Retention(AnnotationRetention.SOURCE)
annotation class PageName {
	companion object {
		const val MAIN = "main"
		const val HOME = "home"
		const val MINE = "mine"
	}
}