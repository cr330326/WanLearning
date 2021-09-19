package com.cryallen.wanlearning.data.remote

import com.cryallen.wanlearning.data.remote.api.WanService

/***
 * 远程数据源管理类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class RemoteRequest {

	val wanService = ServiceCreator.create(WanService::class.java)

	suspend fun getArticles(pageNo: Int) = wanService.getArticles(pageNo)

	suspend fun getTopArticles() = wanService.getTopArticles()

	companion object {

		//单例模式 线程安全
		@Volatile private var remoteNet: RemoteRequest? = null

		fun getInstance(): RemoteRequest {
			if (remoteNet == null) {
				synchronized(RemoteRequest::class.java) {
					if (remoteNet == null) {
						remoteNet = RemoteRequest()
					}
				}
			}
			return remoteNet!!
		}
	}
}