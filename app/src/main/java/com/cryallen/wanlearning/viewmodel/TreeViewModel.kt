package com.cryallen.wanlearning.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.bus.event.SingleLiveEvent
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.repository.RemoteRepository

/***
 * 聚合页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/21
 ***/
class TreeViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	//导航文章列表数据
	var naviDataList = ArrayList<ModelResponse.Navigation>()

	var knowledgeList = ArrayList<ModelResponse.KnowLedgeChapter>()

	private var naviLiveEvent = SingleLiveEvent<Any>()

	private var knowledgeLiveEvent = SingleLiveEvent<Any>()

	//从Repository层获取导航列表数据
	val naviLiveData = Transformations.switchMap(naviLiveEvent) {
		liveData {
			val naviResult = try {
				Result.success(repository.getNaviArticlesData())
			} catch (e: Exception) {
				Result.failure<ApiResponse<ArrayList<ModelResponse.Navigation>>>(e)
			}
			emit(naviResult)
		}
	}

	//从Repository层获取知识体系列表数据
	val knowledgeLiveData = Transformations.switchMap(knowledgeLiveEvent) {
		liveData {
			val treeResult = try {
				Result.success(repository.getKnowledgeData())
			} catch (e: Exception) {
				Result.failure<ApiResponse<ArrayList<ModelResponse.KnowLedgeChapter>>>(e)
			}
			emit(treeResult)
		}
	}

	//获取导航列表数据
	fun onNaviRefresh() {
		naviLiveEvent.value = naviLiveEvent.value
	}

	//获取知识体系列表数据
	fun onKnowledgeRefresh() {
		knowledgeLiveEvent.value = knowledgeLiveEvent.value
	}
}