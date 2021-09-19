package com.cryallen.wanlearning.viewmodel

import com.cryallen.wanlearning.data.remote.RemoteRequest
import com.cryallen.wanlearning.repository.RemoteRepository
import com.cryallen.wanlearning.viewmodel.factory.HomeViewModelFactory

/***
 * ViewModel 依赖注入封装类
 * @author vsh9p8q
 * @DATE 2021/9/19
 ***/
object InjectorProvider {

	private fun getRemoteRepository() = RemoteRepository.getInstance(RemoteRequest.getInstance())

	fun getHomeViewModelFactory() = HomeViewModelFactory(getRemoteRepository())
}