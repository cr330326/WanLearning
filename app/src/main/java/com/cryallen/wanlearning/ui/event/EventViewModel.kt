package com.cryallen.wanlearning.ui.event

import com.cryallen.wanlearning.base.BaseViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/***
 * APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 * @author vsh9p8q
 * @DATE 2021/10/7
 ***/
class EventViewModel : BaseViewModel() {

	//注册并成功后，监听该值的界面都会收到消息
	val registerEvent = UnPeekLiveData<Boolean>()
}