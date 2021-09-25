package com.cryallen.wanlearning.ui.event

import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.utils.CacheUtils
import com.cryallen.wanlearning.utils.GlobalUtils
import com.kunminx.architecture.ui.callback.UnPeekLiveData

/***
 * APP全局的ViewModel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 * @author vsh9p8q
 * @DATE 2021/9/25
 ***/
class AppViewModel {

	//App的账户信息
	var userInfo = UnPeekLiveData.Builder<ModelResponse.UserInfo>().setAllowNullValue(true).create()

	//App主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
	var appColor = UnPeekLiveData<Int>()

	init {
		//默认值保存的账户信息，没有登陆过则为null
		userInfo.value = CacheUtils.getUser()
		//默认值颜色
		appColor.value = GlobalUtils.getThemeColor()
	}
}