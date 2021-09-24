package com.cryallen.wanlearning.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.utils.ActivityCollector
import com.cryallen.wanlearning.utils.GlobalUtils
import com.cryallen.wanlearning.utils.LogUtils
import com.gyf.immersionbar.ImmersionBar
import java.lang.ref.WeakReference
import com.cryallen.wanlearning.ui.activity.ContainerActivity




/***
 * 应用程序中所有Activity的基类。
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
abstract class BaseActivity : AppCompatActivity() {

	/**
	 * 判断当前Activity是否在前台。
	 */
	protected var isActive: Boolean = false

	/**
	 * 当前Activity的实例。
	 */
	protected var activity: Activity? = null

	/** 当前Activity的弱引用，防止内存泄露  */
	private var activityWR: WeakReference<Activity>? = null

	/**
	 * 日志输出标志
	 */
	protected val TAG: String = this.javaClass.simpleName

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		LogUtils.d(TAG,"BaseActivity-->onCreate()")

		activity = this
		//当前的Activity生成一个弱引用，避免内存泄露
		activityWR = WeakReference(activity!!)
		ActivityCollector.pushTask(activityWR)
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		LogUtils.d(TAG, "BaseActivity-->onSaveInstanceState()")
	}

	override fun onRestoreInstanceState(savedInstanceState: Bundle) {
		super.onRestoreInstanceState(savedInstanceState)
		LogUtils.d(TAG, "BaseActivity-->onRestoreInstanceState()")
	}

	override fun onNewIntent(intent: Intent?) {
		super.onNewIntent(intent)
		LogUtils.d(TAG, "BaseActivity-->onNewIntent()")
	}

	override fun onRestart() {
		super.onRestart()
		LogUtils.d(TAG, "BaseActivity-->onRestart()")
	}

	override fun onStart() {
		super.onStart()
		LogUtils.d(TAG, "BaseActivity-->onStart()")
	}

	override fun onResume() {
		super.onResume()
		LogUtils.d(TAG, "BaseActivity-->onResume()")
		isActive = true
	}

	override fun onPause() {
		super.onPause()
		LogUtils.d(TAG, "BaseActivity-->onPause()")
		isActive = false
	}

	override fun onStop() {
		super.onStop()
		LogUtils.d(TAG, "BaseActivity-->onStop()")
	}

	override fun onDestroy() {
		super.onDestroy()
		LogUtils.d(TAG, "BaseActivity-->onDestroy()")

		activity = null
		ActivityCollector.removeTask(activityWR)
	}

	override fun setContentView(layoutResID: Int) {
		super.setContentView(layoutResID)
		setStatusBarBackground()
		setupViews()
	}

	override fun setContentView(layoutView: View) {
		super.setContentView(layoutView)
		setStatusBarBackground()
		setupViews()
	}

	/**
	 * 设置状态栏背景色
	 */
	protected open fun setStatusBarBackground() {
		ImmersionBar.with(this)
			.autoStatusBarDarkModeEnable(true, 0.2f)
			.statusBarColorInt(GlobalUtils.getThemeColor())
			.fitsSystemWindows(true)
			.init()
	}

	/**
	 * 跳转页面
	 *
	 * @param clz    所跳转的目的Activity类
	 * @param bundle 跳转所携带的信息
	 */
	open fun startActivity(clz: Class<*>?, bundle: Bundle?) {
		val intent = Intent(this, clz)
		if (bundle != null) {
			intent.putExtras(bundle)
		}
		startActivity(intent)
	}

	/**
	 * 跳转容器页面
	 *
	 * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
	 */
	open fun startContainerActivity(canonicalName: String?) {
		startContainerActivity(canonicalName, null)
	}

	/**
	 * 跳转容器页面
	 *
	 * @param canonicalName 规范名 : Fragment.class.getCanonicalName()
	 * @param bundle        跳转所携带的信息
	 */
	open fun startContainerActivity(canonicalName: String?, bundle: Bundle?) {
		val intent = Intent(this, ContainerActivity::class.java)
		intent.putExtra(CommonConfig.CONTAINER_FRAGMENT, canonicalName)
		if (bundle != null) {
			intent.putExtra(CommonConfig.CONTAINER_BUNDLE, bundle)
		}
		startActivity(intent)
	}

	protected open fun setupViews() {

	}
}
