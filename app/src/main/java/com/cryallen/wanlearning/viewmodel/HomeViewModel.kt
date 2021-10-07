package com.cryallen.wanlearning.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.bus.event.SingleLiveEvent
import com.cryallen.wanlearning.model.bean.ApiPagerResponse
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow

/***
 * 首页页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	//页码 首页数据页码从0开始
	var pageNo = 0

	//首页文章列表数据
	var articleDataList = ArrayList<ModelResponse.Article>()

	private var articleLiveEvent = SingleLiveEvent<Int>()

	private var bannerLiveEvent = SingleLiveEvent<Any>()

	//基于Paging组件获取数据源
	fun getArticlePagingData(): Flow<PagingData<ModelResponse.Article>> {
		return repository.getHomePagingData().cachedIn(viewModelScope)
	}

	//从Repository层获取首页和置顶文章数据
	val articleLiveData = Transformations.switchMap(articleLiveEvent) {
		liveData {
			val articleResult = try {
				Result.success(repository.getHomeData(it!!))
			} catch (e: Exception) {
				Result.failure<ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>>(e)
			}
			emit(articleResult)
		}
	}

	//从Repository层获取轮播图数据
	val bannerLiveData = Transformations.switchMap(bannerLiveEvent) {
		liveData {
			val bannerResult = try {
				Result.success(repository.getBannerData())
			} catch (e: Exception) {
				Result.failure<ApiResponse<List<ModelResponse.Banner>>>(e)
			}
			emit(bannerResult)
		}
	}

	//获取首页和置顶文章数据
	fun onArticleRefresh(isRefresh: Boolean) {
		if(isRefresh){
			pageNo = 0
		}
		articleLiveEvent.value = pageNo
	}

	//获取轮播图数据
	fun onBannerRefresh() {
		bannerLiveEvent.value = bannerLiveEvent.value
	}
}