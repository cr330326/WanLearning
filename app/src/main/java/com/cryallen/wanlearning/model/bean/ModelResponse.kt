package com.cryallen.wanlearning.model.bean

import java.io.Serializable

/***
 * 服务器响应类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class ModelResponse {

	/***
	 * 文章数据类
	 ***/
	data class Article(
		var apkLink: String,
		var author: String,
		var chapterId: Int,
		var chapterName: String,
		var collect: Boolean,
		var courseId: Int,
		var desc: String,
		var envelopePic: String,
		var fresh: Boolean,
		var id: Int,
		var link: String,
		var niceDate: String,
		var origin: String,
		var projectLink: String,
		var publishTime: Long,
		var superChapterId: Int,
		var superChapterName: String,
		var title: String,
		var type: Int,
		var userId: Int,
		var visible: Int,
		var zan: Int,
		var tags: List<Tag>,
		var isTop: Boolean,
		var bannerData: List<Banner>
	)

	data class Tag(
		var name: String,
		var url: String
	)

	/***
	 * 公众号数据类
	 ***/
	data class Chapter(
		var children: List<Any>,
		var courseId: Int,
		var id: Int,
		var name: String,
		var order: Int,
		var parentChapterId: Int,
		var userControlSetTop: Boolean,
		var visible: Int
	) : Serializable

	data class Navigation(
		var articles: List<Article>,
		var cid: Int,
		var name: String
	)

	/***
	 * 轮播图数据类
	 ***/
	data class Banner(
		var desc: String,
		var id: Int,
		var imagePath: String,
		var isVisible: Int,
		var order: Int,
		var title: String,
		var type: Int,
		var url: String
	)

	/***
	 * 用户数据类
	 ***/
	data class UserInfo(var admin: Boolean = false,
	                    var chapterTops: List<String> = listOf(),
	                    var collectIds: MutableList<String> = mutableListOf(),
	                    var email: String="",
	                    var icon: String="",
	                    var id: String="",
	                    var nickname: String="",
	                    var password: String="",
	                    var token: String="",
	                    var type: Int =0,
	                    var username: String="")
}