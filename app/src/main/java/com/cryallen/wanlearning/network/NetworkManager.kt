package com.cryallen.wanlearning.network

import com.cryallen.wanlearning.network.api.WanService

/***
 * 管理所有网络请求。
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class NetworkManager {

	val wanService = ServiceCreator.create(WanService::class.java)

	companion object {

		//单例模式 线程安全
		private var network: NetworkManager? = null

		fun getInstance(): NetworkManager {
			if (network == null) {
				synchronized(NetworkManager::class.java) {
					if (network == null) {
						network = NetworkManager()
					}
				}
			}
			return network!!
		}
	}
}