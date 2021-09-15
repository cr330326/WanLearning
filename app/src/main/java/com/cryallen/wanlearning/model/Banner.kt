package com.cryallen.wanlearning.model

/***
 * 首页banner数据类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
data class Banner(val `data`: List<Data>, val errorCode: Int, val errorMsg: String) : Model() {

	data class Data(
		val desc: String,
		val id: Int,
		val imagePath: String,
		val isVisible: Int,
		val order: Int,
		val title: String,
		val type: Int,
		val url: String
	)
}
