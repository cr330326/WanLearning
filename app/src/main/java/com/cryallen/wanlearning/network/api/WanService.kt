package com.cryallen.wanlearning.network.api

import com.cryallen.wanlearning.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/***
 * 应用服务端API地址
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
interface WanService {

	/**
	 * 首页文章列表
	 */
	@GET("article/list/{pageNo}/json")
	suspend fun getArticles(@Path("pageNo") pageNo: Int): Articles

	/**
	 * 置顶文章列表
	 */
	@GET("article/top/json")
	suspend fun getTopArticles(): ArticleTop

	/**
	 * 首页 banner
	 */
	@GET("banner/json")
	suspend fun getBanner(): Banner

	/**
	 * 公众号列表
	 */
	@GET("wxarticle/chapters/json")
	suspend fun getWXChapters(): Chapters

	/**
	 * 查看某个公众号历史数据
	 */
	@GET("wxarticle/list/{id}/{pageNo}/json")
	suspend fun getWXArticles(@Path("id") id: Int, @Path("pageNo") pageNo: Int): ChapterDetail

	/**
	 * 项目类目列表
	 */
	@GET("project/tree/json")
	suspend fun getProjects(): Chapters

	/**
	 * 项目文章列表
	 */
	@GET("project/list/{pageNo}/json")
	suspend fun getProjectArticles(@Path("pageNo") pageNo: Int, @Query("cid") cid: Int): Articles

	/**
	 * 导航数据
	 */
	@GET("navi/json")
	suspend fun getProjectArticles(): Navigation

	/**
	 * 知识体系
	 */
	@GET("tree/json")
	suspend fun getKnowledgeTree(): Chapters

	/**
	 * 知识体系文章列表
	 */
	@GET("article/list/{pageNo}/json")
	suspend fun getKnowledgeArticles(@Path("pageNo") pageNo: Int, @Query("cid") cid: Int): Articles
}