package com.cryallen.wanlearning.data.remote.api

import com.cryallen.wanlearning.model.bean.ApiPagerResponse
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
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
	@GET("article/list/{pageNo}/json?page_size=20")
	suspend fun getArticles(@Path("pageNo") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>

	/**
	 * 置顶文章列表
	 */
	@GET("article/top/json")
	suspend fun getTopArticles(): ApiResponse<List<ModelResponse.Article>>

	/**
	 * 首页 banner
	 */
	@GET("banner/json")
	suspend fun getBanner(): ApiResponse<List<ModelResponse.Banner>>

	/**
	 * 公众号列表
	 */
	@GET("wxarticle/chapters/json")
	suspend fun getWXChapters(): ApiResponse<ArrayList<ModelResponse.Chapter>>

	/**
	 * 查看某个公众号历史数据
	 */
	@GET("wxarticle/list/{id}/{pageNo}/json")
	suspend fun getWXArticles(@Path("id") id: Int, @Path("pageNo") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>

	/**
	 * 项目类目列表
	 */
	@GET("project/tree/json")
	suspend fun getProjects(): ApiResponse<ArrayList<ModelResponse.Chapter>>

	/**
	 * 项目文章列表
	 */
	@GET("project/list/{pageNo}/json")
	suspend fun getProjectArticles(@Path("pageNo") pageNo: Int, @Query("cid") cid: Int): ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>

	/**
	 * 导航数据
	 */
	@GET("navi/json")
	suspend fun getProjectArticles(): ApiResponse<ArrayList<ModelResponse.Navigation>>
}