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
import com.cryallen.wanlearning.utils.LogUtils
import kotlinx.coroutines.flow.Flow

/***
 * 首页页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	var articleDataList = ArrayList<ModelResponse.Article>()

	private var articleLiveEvent = SingleLiveEvent<Int>()

	//基于Paging组件获取数据源
	fun getArticlePagingData(): Flow<PagingData<ModelResponse.Article>> {
		return repository.getHomePagingData().cachedIn(viewModelScope)
	}

	//获取首页和置顶文章数据
	val articleLiveData = Transformations.switchMap(articleLiveEvent) {
		liveData {
			val result = try {
				LogUtils.d("start request")
				val articleList = repository.getHomeData(it!!)
				Result.success(articleList)
			} catch (e: Exception) {
				Result.failure<ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>>(e)
			}
			emit(result)
		}
	}

	fun onRefresh() {
		articleLiveEvent.value = 1
	}

	inner class RequestParam(val pageNo : Int)
}