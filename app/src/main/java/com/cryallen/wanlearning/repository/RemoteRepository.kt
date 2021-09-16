package com.cryallen.wanlearning.repository

import com.cryallen.wanlearning.model.bean.ApiPagerResponse
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.data.remote.RemoteRequest
import com.cryallen.wanlearning.utils.CacheUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/***
 * Repository层，数据源从远程网络获取
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class RemoteRepository {

	/**
	 * 获取首页文章数据
	 */
	suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>> {
		//同时异步请求2个接口，请求完成后合并数据
		return withContext(Dispatchers.IO) {
			val listData = async { RemoteRequest.getInstance().wanService.getArticles(pageNo) }
			//如果App配置打开了首页请求置顶文章，且是第一页
			if (CacheUtil.isNeedTop() && pageNo == 0) {
				val topData = async { RemoteRequest.getInstance().wanService.getTopArticles() }
				listData.await().data.datas.addAll(0, topData.await().data)
				listData.await()
			} else {
				listData.await()
			}
		}
	}
}