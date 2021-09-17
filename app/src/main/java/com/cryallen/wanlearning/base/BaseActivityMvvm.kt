package com.cryallen.wanlearning.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/***
 * 基于MVVM模式下的基类Activity
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
abstract class BaseActivityMvvm<V : ViewDataBinding, VM : BaseViewModel> : BaseActivity(), IBaseViewMvvm {
	private lateinit var binding: V
	private lateinit var viewModel: VM
	private var viewModelId = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		//页面接受的参数方法
		initParam()
		//私有的初始化 binding和ViewModel方法
		initViewDataBinding(savedInstanceState)

		initToolbar()
		//页面数据初始化方法
		initData()
		//页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
		initViewObservable()
	}

	override fun onDestroy() {
		super.onDestroy()
		binding.unbind()
	}

	/**
	 * 注入绑定
	 */
	private fun initViewDataBinding(savedInstanceState: Bundle?) {
		//DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.binding包
		binding = DataBindingUtil.setContentView(this, initContentView(savedInstanceState))

		viewModelId = initVariableId()
		binding.lifecycleOwner = this
		val modelClass: Class<VM>
		val type = javaClass.genericSuperclass

		modelClass = if (type is ParameterizedType) {
			type.actualTypeArguments[1] as Class<VM>
		} else {
			//如果没有指定泛型参数，则默认使用BaseViewModelMvvm
			BaseViewModel::class.java as Class<VM>
		}
		viewModel = createViewModel(this, modelClass)
		//关联ViewModel
		binding.setVariable(viewModelId, viewModel)
		//让ViewModel拥有View的生命周期感应
		lifecycle.addObserver(viewModel)
	}

	//刷新布局数据
	fun refreshLayout() {
		binding.setVariable(viewModelId, viewModel)
	}

	override fun initParam() {}

	/**
	 * 初始化根布局
	 *
	 * @return 布局layout的id
	 */
	abstract fun initContentView(savedInstanceState: Bundle?): Int

	/**
	 * 初始化ViewModel的id
	 *
	 * @return BR的id
	 */
	abstract fun initVariableId(): Int

	open fun initToolbar() {}
	override fun initData() {}
	override fun initViewObservable() {
		//私有的ViewModel与View的契约事件回调逻辑
	}

	/**
	 * @param cls 类
	 * @param <T> 泛型参数,必须继承ViewMode
	 * @return 生成的viewMode实例
	</T> */
	private fun <T : ViewModel> createViewModel(activity: FragmentActivity, cls: Class<T>): T {
		return ViewModelProvider(activity).get(cls)
	}
}