package com.cryallen.wanlearning.model

/***
 * 公众号列表数据类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
data class Chapters(val `data`: List<Data>, val errorCode: Int, val errorMsg: String) : Model() {

	data class Data(
		val children: List<Any>,
		val courseId: Int,
		val id: Int,
		val name: String,
		val order: Int,
		val parentChapterId: Int,
		val userControlSetTop: Boolean,
		val visible: Int
	)
}

