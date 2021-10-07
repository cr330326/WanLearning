package com.cryallen.wanlearning

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.cryallen.wanlearning.ui.event.AppViewModel
import com.cryallen.wanlearning.ui.event.EventViewModel
import com.cryallen.wanlearning.ui.view.loadcallback.EmptyCallback
import com.cryallen.wanlearning.ui.view.loadcallback.ErrorCallback
import com.cryallen.wanlearning.ui.view.loadcallback.LoadingCallback
import com.cryallen.wanlearning.utils.LogUtils
import com.cryallen.wanlearning.utils.LoggerUtils
import com.cryallen.wanlearning.utils.XKeyValue
import com.kingja.loadsir.core.LoadSir
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/***
 * 应用Application页面
 * @author vsh9p8q
 * @DATE 2021/9/12
 ***/

//Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
val appViewModel: AppViewModel by lazy { WanApplication.appViewModelInstance }

//Application全局的ViewModel，用于发送全局通知操作
val eventViewModel: EventViewModel by lazy { WanApplication.eventViewModelInstance }

class WanApplication : Application(), ViewModelStoreOwner {

	init {
		//默认打开日志开关
		LogUtils.setDebuggable(true)
		LogUtils.setTagName("WanApp_Logger")
		LoggerUtils.init(true)

		//设置默认 Refresh 初始化
		SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
			layout.setDragRate(0.7f)
			layout.setEnableLoadMoreWhenContentNotFull(true)
			layout.setEnableScrollContentWhenLoaded(true)
			layout.setEnableOverScrollBounce(true)
		}

		//设置全局的Header构建器
		SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
			layout.setEnableHeaderTranslationContent(true)
			layout.setHeaderTriggerRate(0.6f)
			MaterialHeader(context).setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary)
		}

		//设置全局的Footer构建器
		SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
			layout.setFooterTriggerRate(0.6f)
			layout.setEnableFooterFollowWhenNoMoreData(true)
			layout.setEnableFooterTranslationContent(true)
			ClassicsFooter(context)
		}
	}

	private lateinit var mAppViewModelStore: ViewModelStore

	private var mFactory: ViewModelProvider.Factory? = null

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
	}

	override fun getViewModelStore(): ViewModelStore {
		return mAppViewModelStore
	}

	override fun onCreate() {
		super.onCreate()
		LogUtils.d("Logger", "-----------WanApplication Beginning-----------")
		XKeyValue.init(this)

		mAppViewModelStore = ViewModelStore()

		instance = this
		appViewModelInstance = getAppViewModelProvider().get(AppViewModel::class.java)
		eventViewModelInstance = getAppViewModelProvider().get(EventViewModel::class.java)

		//界面加载管理 初始化
		LoadSir.beginBuilder()
			.addCallback(LoadingCallback()) //加载页面
			.addCallback(ErrorCallback())   //错误页面
			.addCallback(EmptyCallback())   //空页面
			.commit()
	}

	/**
	 * 获取一个全局的ViewModel
	 */
	private fun getAppViewModelProvider(): ViewModelProvider {
		return ViewModelProvider(this, this.getAppFactory())
	}

	private fun getAppFactory(): ViewModelProvider.Factory {
		if (mFactory == null) {
			mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
		}
		return mFactory as ViewModelProvider.Factory
	}

	companion object {
		lateinit var instance: Application
		lateinit var eventViewModelInstance: EventViewModel
		lateinit var appViewModelInstance: AppViewModel
	}

}