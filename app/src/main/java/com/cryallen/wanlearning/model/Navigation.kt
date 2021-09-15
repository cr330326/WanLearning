package com.cryallen.wanlearning.model

/***
 * 导航项目数据类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
data class Navigation(val `data`: List<Data>, val errorCode: Int, val errorMsg: String) : Model() {

	data class Data(
		val articles: List<Article>,
		val cid: Int,
		val name: String
	)

	data class Article(
		val apkLink: String,
		val audit: Int,
		val author: String,
		val canEdit: Boolean,
		val chapterId: Int,
		val chapterName: String,
		val collect: Boolean,
		val courseId: Int,
		val desc: String,
		val descMd: String,
		val envelopePic: String,
		val fresh: Boolean,
		val host: String,
		val id: Int,
		val link: String,
		val niceDate: String,
		val niceShareDate: String,
		val origin: String,
		val prefix: String,
		val projectLink: String,
		val publishTime: Long,
		val realSuperChapterId: Int,
		val selfVisible: Int,
		val shareDate: Any,
		val shareUser: String,
		val superChapterId: Int,
		val superChapterName: String,
		val tags: List<Tag>,
		val title: String,
		val type: Int,
		val userId: Int,
		val visible: Int,
		val zan: Int
	)

	data class Tag(
		var name: String,
		var url: String
	)
}

