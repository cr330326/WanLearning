package com.cryallen.wanlearning

import android.app.Application
import android.content.Context
import com.cryallen.wanlearning.ui.view.loadCallBack.EmptyCallback
import com.cryallen.wanlearning.ui.view.loadCallBack.ErrorCallback
import com.cryallen.wanlearning.ui.view.loadCallBack.LoadingCallback
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
class WanApplication : Application() {

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

	override fun attachBaseContext(base: Context?) {
		super.attachBaseContext(base)
	}

	override fun onCreate() {
		super.onCreate()
		LogUtils.d("Logger", "-----------WanApplication Beginning-----------")
		instance = this
		XKeyValue.init(this)

		//界面加载管理 初始化
		LoadSir.beginBuilder()
			.addCallback(LoadingCallback()) //加载页面
			.addCallback(ErrorCallback())   //错误页面
			.addCallback(EmptyCallback())   //空页面
			.commit()
	}

	companion object {
		lateinit var instance: Application
	}

}