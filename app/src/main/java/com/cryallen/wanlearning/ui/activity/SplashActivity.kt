package com.cryallen.wanlearning.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.cryallen.wanlearning.base.BaseActivity
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.databinding.ActivitySplashBinding
import com.cryallen.wanlearning.utils.XKeyValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/***
 * 闪屏页面，应用程序首次启动入口。
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
class SplashActivity : BaseActivity() {

	private var _binding: ActivitySplashBinding? = null

	private val binding: ActivitySplashBinding
		get() = _binding!!

	//延迟3S进入首页
	private val splashDuration = 3 * 1000L

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivitySplashBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	override fun setupViews() {
		lifecycleScope.launch {
			delay(splashDuration)
			MainActivity.start(this@SplashActivity)
			finish()
		}
		isFirstEntryApp = false
	}

	override fun setStatusBarBackground() {}

	companion object {

		/**
		 * 是否首次进入APP应用
		 */
		var isFirstEntryApp: Boolean
			get() = XKeyValue.get(CommonConfig.SPLASH_IS_FIRST_ENTRY_APP, true)
			set(value) {
				CoroutineScope(Dispatchers.IO).launch { XKeyValue.put(CommonConfig.SPLASH_IS_FIRST_ENTRY_APP, value) }
			}
	}
}