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

	suspend fun getBanner() = wanService.getBanner()

	suspend fun getWXChapters() = wanService.getWXChapters()

	suspend fun getWXArticles(cId: Int, pageNo: Int) = wanService.getWXArticles(cId,pageNo)

	suspend fun getProjects() = wanService.getProjects()

	suspend fun getProjectArticles(pageNo: Int,cId: Int) = wanService.getProjectArticles(pageNo,cId)

	suspend fun getNaviArticles() = wanService.getNaviArticles()

	suspend fun getKnowledgeTree() = wanService.getKnowledgeTree()

	suspend fun getIntegral() = wanService.getIntegral()

	suspend fun login(username : String, pwd: String) = wanService.login(username,pwd)

	suspend fun register(username : String, pwd: String, confirmPwd: String) = wanService.register(username,pwd,confirmPwd)

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