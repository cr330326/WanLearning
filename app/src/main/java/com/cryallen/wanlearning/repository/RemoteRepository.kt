package com.cryallen.wanlearning.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.data.remote.RemoteRequest
import com.cryallen.wanlearning.paging.HomePagingSource
import com.cryallen.wanlearning.utils.CacheUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/***
 * Repository层，数据源从远程网络获取
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class RemoteRepository private constructor(private val remoteRequest: RemoteRequest) {

	/**
	 * 获取首页和置顶文章数据
	 */
	suspend fun getHomeData(pageNo: Int) = requestHomeData(pageNo)

	/**
	 * 获取首页Banner数据
	 */
	suspend fun getBannerData() = requestBannerData()

	/**
	 * 获取公众号标题列表数据
	 */
	suspend fun getWxChapterData() = requestWXChapterData()

	/**
	 * 获取公众号标题列表数据
	 */
	suspend fun getWxArticlesData(cId: Int, pageNo: Int) = requestWXArticlesData(cId,pageNo)

	/**
	 * 获取项目标题列表数据
	 */
	suspend fun getProjectData() = requestProjectData()

	/**
	 * 获取项目列表列表数据
	 */
	suspend fun getProjectArticlesData(pageNo: Int, cId: Int) = requestProjectArticles(pageNo,cId)

	/**
	 * 获取首页文章数据，基于Pageing3组件开发
	 */
	fun getHomePagingData(): Flow<PagingData<ModelResponse.Article>> {
		return Pager(
			config = PagingConfig(enablePlaceholders = false, pageSize = CommonConfig.PAGING_STARTING_PAGE_SIZE),
			pagingSourceFactory = { HomePagingSource(remoteRequest.wanService) }
		).flow
	}

	/**
	 * 获取首页和置顶文章数据
	 */
	private suspend fun requestHomeData(pageNo: Int) = withContext(Dispatchers.IO) {
		coroutineScope {
			//同时异步请求2个接口，请求完成后合并数据
			val articleData = async { remoteRequest.getArticles(pageNo) }
			if (CacheUtil.isNeedTop() && pageNo == 0) {
				val articleTopData = async { remoteRequest.getTopArticles() }
				articleData.await().data.datas.addAll(0, articleTopData.await().data)
			} 
			articleData.await()
		}
	}

	/**
	 * 获取首页Banner数据
	 */
	private suspend fun requestBannerData() = withContext(Dispatchers.IO) {
		coroutineScope {
			val bannerData = async { remoteRequest.getBanner() }
			bannerData.await()
		}
	}

	/**
	 * 获取公众号标题数据
	 */
	private suspend fun requestWXChapterData() = withContext(Dispatchers.IO) {
		coroutineScope {
			val wxChaptersData = async { remoteRequest.getWXChapters() }
			wxChaptersData.await()
		}
	}

	/**
	 * 获取公众号章节数据
	 */
	private suspend fun requestWXArticlesData(cId: Int, pageNo: Int) = withContext(Dispatchers.IO) {
		coroutineScope {
			val wxArticlesData = async { remoteRequest.getWXArticles(cId,pageNo) }
			wxArticlesData.await()
		}
	}

	/**
	 * 获取项目标题数据
	 */
	private suspend fun requestProjectData() = withContext(Dispatchers.IO) {
		coroutineScope {
			val projectData = async { remoteRequest.getProjects() }
			projectData.await()
		}
	}

	/**
	 * 获取项目列表数据
	 */
	private suspend fun requestProjectArticles(pageNo: Int, cId: Int) = withContext(Dispatchers.IO) {
		coroutineScope {
			val projectArticlesData = async { remoteRequest.getProjectArticles(pageNo,cId) }
			projectArticlesData.await()
		}
	}

	companion object {

		private var repository: RemoteRepository? = null

		fun getInstance(remote: RemoteRequest): RemoteRepository {
			if (repository == null) {
				synchronized(RemoteRepository::class.java) {
					if (repository == null) {
						repository = RemoteRepository(remote)
					}
				}
			}

			return repository!!
		}
	}
}