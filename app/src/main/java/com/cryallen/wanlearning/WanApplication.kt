package com.cryallen.wanlearning

import android.app.Application
import android.content.Context
import com.cryallen.wanlearning.utils.LogUtils
import com.cryallen.wanlearning.utils.XKeyValue
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/***
 * 应用Application页面
 * @author vsh9p8q
 * @DATE 2021/9/12
 ***/
class WanApplication : Application() {

	init {
		//匿名函数写法
		/*SmartRefreshLayout.setDefaultRefreshInitializer(fun (context:Context,refreshLayout: RefreshLayout) : Unit {
			refreshLayout.setEnableLoadMore(true)
			refreshLayout.setEnableLoadMoreWhenContentNotFull(true)
		});*/
		//默认打开日志开关
		LogUtils.setDebuggable(true)
		LogUtils.setTagName("WanApp_Logger")

		//Lambda表达式写法
		SmartRefreshLayout.setDefaultRefreshInitializer{ context, layout ->
			layout.setEnableLoadMore(true)
			layout.setEnableLoadMoreWhenContentNotFull(true)
		}
	}

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
	}

	override fun onCreate() {
		super.onCreate()
		LogUtils.d("Logger", "-----------WanApplication Beginning-----------")
		instance = this
		XKeyValue.init(this)
	}

	companion object {
		lateinit var instance: Application
	}

}