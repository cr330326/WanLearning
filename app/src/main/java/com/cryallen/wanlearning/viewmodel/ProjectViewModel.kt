package com.cryallen.wanlearning.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.bus.event.SingleLiveEvent
import com.cryallen.wanlearning.model.bean.ApiPagerResponse
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.repository.RemoteRepository

/***
 * 项目页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class ProjectViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	//页码 数据页码从0开始
	var pageNo = 0

	//fragment集合
	var fragments: ArrayList<Fragment> = arrayListOf()

	//标题集合
	var titleList: ArrayList<String> = arrayListOf()

	//项目文章列表数据
	var articleDataList = ArrayList<ModelResponse.Article>()

	private var projectTitleLiveEvent = SingleLiveEvent<Any>()

	private var articleLiveEvent = SingleLiveEvent<RequestParam>()

	//从Repository层获取项目标题数据
	val projectTitleLiveData = Transformations.switchMap(projectTitleLiveEvent) {
		liveData {
			val projectResult = try {
				Result.success(repository.getProjectData())
			} catch (e: Exception) {
				Result.failure<ApiResponse<ArrayList<ModelResponse.Chapter>>>(e)
			}
			emit(projectResult)
		}
	}

	//从Repository层获取项目列表数据
	val articleLiveData = Transformations.switchMap(articleLiveEvent) {
		liveData {
			val articleResult = try {
				Result.success(repository.getProjectArticlesData(it!!.pageNo,it.cId))
			} catch (e: Exception) {
				Result.failure<ApiResponse<ApiPagerResponse<ArrayList<ModelResponse.Article>>>>(e)
			}
			emit(articleResult)
		}
	}

	//获取项目标题数据
	fun onProjectTitleRefresh() {
		projectTitleLiveEvent.value = projectTitleLiveEvent.value
	}

	//获取文章列表数据
	fun onArticleRefresh(isRefresh: Boolean, cid: Int) {
		if(isRefresh){
			pageNo = 0
		}
		articleLiveEvent.value = RequestParam(pageNo,cid)
	}

	inner class RequestParam(val pageNo: Int, val cId: Int)
}