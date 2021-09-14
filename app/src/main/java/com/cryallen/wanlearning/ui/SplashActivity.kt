package com.cryallen.wanlearning.ui

import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
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

	private val splashDuration = 3 * 1000L

	private val alphaAnimation by lazy {
		AlphaAnimation(0.5f, 1.0f).apply {
			duration = splashDuration
			fillAfter = true
		}
	}

	private val scaleAnimation by lazy {
		ScaleAnimation(1f, 1.05f, 1f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
			duration = splashDuration
			fillAfter = true
		}
	}

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
		super.setupViews()
		binding.ivSlogan.startAnimation(alphaAnimation)
		binding.ivSplashPicture.startAnimation(scaleAnimation)
		lifecycleScope.launch {
			delay(splashDuration)
			MainActivity.start(this@SplashActivity)
			finish()
		}
		isFirstEntryApp = false
	}

	override fun setStatusBarBackground(statusBarColor: Int) {}

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