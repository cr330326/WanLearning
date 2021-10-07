package com.cryallen.wanlearning.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.ui.activity.ContainerActivity
import com.cryallen.wanlearning.ui.ext.loadServiceInit
import com.cryallen.wanlearning.utils.LogUtils
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.core.LoadService

/***
 * 应用程序中所有Fragment的基类。
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
abstract class BaseFragment : Fragment() {

	protected lateinit var loadService: LoadService<Any>

	/**
	 * 是否已经加载过数据
	 */
	private var mHasLoadedData = false

	/**
	 * Fragment中inflate出来的布局。
	 */
	protected var rootView: View? = null

	/**
	 * 依附的Activity
	 */
	lateinit var activity: Activity

	/**
	 * 日志输出标志
	 */
	protected val TAG: String = this.javaClass.simpleName

	override fun onAttach(context: Context) {
		super.onAttach(context)
		// 缓存当前依附的activity
		activity = requireActivity()
		LogUtils.d(TAG, "BaseFragment-->onAttach()")
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		LogUtils.d(TAG, "BaseFragment-->onCreate()")
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		LogUtils.d(TAG, "BaseFragment-->onCreateView()")
		return super.onCreateView(inflater, container, savedInstanceState)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		LogUtils.d(TAG, "BaseFragment-->onViewCreated()")
		initView(savedInstanceState)
		createObserver()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		LogUtils.d(TAG, "BaseFragment-->onActivityCreated()")
	}

	override fun onStart() {
		super.onStart()
		LogUtils.d(TAG, "BaseFragment-->onStart()")
	}

	override fun onResume() {
		super.onResume()
		LogUtils.d(TAG, "BaseFragment-->onResume()")
		//当Fragment在屏幕上可见并且没有加载过数据时调用
		if (!mHasLoadedData) {
			loadDataOnce()
			LogUtils.d(TAG, "BaseFragment-->loadDataOnce()")
			mHasLoadedData = true
		}
	}

	override fun onPause() {
		super.onPause()
		LogUtils.d(TAG, "BaseFragment-->onPause()")
	}

	override fun onStop() {
		super.onStop()
		LogUtils.d(TAG, "BaseFragment-->onStop()")
	}

	override fun onDestroyView() {
		super.onDestroyView()
		LogUtils.d(TAG, "BaseFragment-->onDestroyView()")
		if (rootView?.parent != null) (rootView?.parent as ViewGroup).removeView(rootView)
	}

	override fun onDestroy() {
		super.onDestroy()
		//必须调用该方法，防止内存泄漏
		ImmersionBar.destroy(this)
		LogUtils.d(TAG, "BaseFragment-->onDestroy()")
	}

	override fun onDetach() {
		super.onDetach()
		LogUtils.d(TAG, "BaseFragment-->onDetach()")
	}

	open fun isBackPressed(): Boolean {
		return false
	}

	/**
	 * 跳转页面
	 *
	 * @param clz 所跳转的目的Activity类
	 */
	fun startActivity(clz: Class<*>?) {
		startActivity(Intent(context, clz))
	}

	/**
	 * 跳转页面
	 *
	 * @param clz    所跳转的目的Activity类
	 * @param bundle 跳转所携带的信息
	 */
	fun startActivity(clz: Class<*>?, bundle: Bundle?) {
		val intent = Intent(context, clz)
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
		val intent = Intent(context, ContainerActivity::class.java)
		intent.putExtra(CommonConfig.CONTAINER_FRAGMENT, canonicalName)
		if (bundle != null) {
			intent.putExtra(CommonConfig.CONTAINER_BUNDLE, bundle)
		}
		startActivity(intent)
	}

	/**
	 * 在Fragment基类中获取通用的控件，会将传入的View实例原封不动返回。
	 * @param view Fragment中inflate出来的View实例。
	 * @return  Fragment中inflate出来的View实例原封不动返回。
	 */
	fun onCreateView(view: View): View {
		LogUtils.d(TAG, "BaseFragment-->onCreateView()")
		rootView = view
		return view
	}

	/**
	 * 设置加载加载框
	 */
	open fun setLoadSir(parentView: View,callback: () -> Unit) {
		loadService = loadServiceInit(parentView,callback)
	}

	/**
	 * 初始化view
	 */
	abstract fun initView(savedInstanceState: Bundle?)

	/**
	 * 创建LiveData观察者 Fragment执行onViewCreated后触发
	 */
	abstract fun createObserver()

	/**
	 * 页面首次可见时调用一次该方法，在这里可以请求网络数据等。
	 */
	open fun loadDataOnce() {

	}
}