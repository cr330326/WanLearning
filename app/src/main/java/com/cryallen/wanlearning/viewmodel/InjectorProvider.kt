package com.cryallen.wanlearning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.data.remote.RemoteRequest
import com.cryallen.wanlearning.repository.RemoteRepository

/***
 * ViewModel 依赖注入封装类
 * @author vsh9p8q
 * @DATE 2021/9/19
 ***/
object InjectorProvider {

	private fun getRemoteRepository() = RemoteRepository.getInstance(RemoteRequest.getInstance())

	fun getHomeViewModelFactory() = HomeViewModelFactory(getRemoteRepository())

	fun getWechatViewModelFactory() = WechatViewModelFactory(getRemoteRepository())

	fun getProjectViewModelFactory() = ProjectViewModelFactory(getRemoteRepository())

	fun getTreeViewModelFactory() = TreeViewModelFactory(getRemoteRepository())
}

/***
 * 首页ViewModelFactory
 ***/
class HomeViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return HomeViewModel(repository) as T
	}
}

/***
 * 公众号ViewModelFactory
 ***/
class WechatViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return WechatViewModel(repository) as T
	}
}


/***
 * 项目ViewModelFactory
 ***/
class ProjectViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return ProjectViewModel(repository) as T
	}
}


/***
 * 聚合ViewModelFactory
 ***/
class TreeViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.NewInstanceFactory() {

	@Suppress("UNCHECKED_CAST")
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		return TreeViewModel(repository) as T
	}
}