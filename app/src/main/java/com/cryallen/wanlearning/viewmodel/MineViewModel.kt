package com.cryallen.wanlearning.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.bus.event.SingleLiveEvent
import com.cryallen.wanlearning.model.bean.ApiResponse
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.repository.RemoteRepository

/***
 * 个人页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/25
 ***/
class MineViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	private var integralLiveEvent = SingleLiveEvent<Any>()

	//从Repository层获取个人积分数据
	val integralLiveData = Transformations.switchMap(integralLiveEvent) {
		liveData {
			val integralResult = try {
				Result.success(repository.getIntegral())
			} catch (e: Exception) {
				Result.failure<ApiResponse<ModelResponse.IntegralInfo>>(e)
			}
			emit(integralResult)
		}
	}

	//获取个人积分数据
	fun onMineRefresh() {
		integralLiveEvent.value = integralLiveEvent.value
	}

}