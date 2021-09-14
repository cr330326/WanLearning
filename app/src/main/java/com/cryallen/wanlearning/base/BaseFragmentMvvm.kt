package com.cryallen.wanlearning.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.reflect.ParameterizedType

/***
 * 基于MVVM模式下的基类的Fragment
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
abstract class BaseFragmentMvvm<V : ViewDataBinding, VM : BaseViewModelMvvm> : BaseFragment(), IBaseViewMvvm {
	private lateinit var binding: V
	private lateinit var viewModel: VM
	private var viewModelId = 0
	private var isNavigationViewInit = false // 记录是否已经初始化过一次视图

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initParam()
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		//如果fragment的view已经创建则不再重新创建
		if (rootView == null) {
			binding = DataBindingUtil.inflate(inflater, initContentView(inflater, container, savedInstanceState), container, false)
			binding.lifecycleOwner = this
			rootView = binding.root
			onCreateView(binding.root)
		}
		return rootView
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		if (!isNavigationViewInit) { //初始化过视图则不再进行view和data初始化
			super.onViewCreated(view, savedInstanceState)
			//私有的初始化DataBinding和ViewModel方法
			initViewDataBinding()
			//页面数据初始化方法
			initData()
			initToolbar()
			//页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
			initViewObservable()
		}
		isNavigationViewInit = true
	}

	/**
	 * 注入绑定
	 */
	private fun initViewDataBinding() {
		viewModelId = initVariableId()
		val modelClass: Class<VM>
		//返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type
		//然后将其转换ParameterizedType
		val type = javaClass.genericSuperclass
		modelClass = if (type is ParameterizedType) {
			// 返回表示此类型实际类型参数的 Type 对象的数组。简而言之就是获得超类的泛型参数的实际类型,获取第二个泛型
			type.actualTypeArguments[1] as Class<VM>
		} else {
			//如果没有指定泛型参数，则默认使用BaseViewModel
			BaseViewModelMvvm::class.java as Class<VM>
		}
		viewModel = createViewModel(this, modelClass)

		binding.setVariable(viewModelId, viewModel)
		/*
		 * 让ViewModel拥有View的生命周期感应
		 * viewModel implements IBaseViewModel接口
		 * IBaseViewModelMvvm extends LifecycleObserver
		 * 所以ViewModel是lifecycle生命周期的观察者,viewModel可以在不同的生命周期中处理不同的事情
		 * viewModel可以感受到ui的生命周期状态;
		 * BaseViewModel中实现了IBaseViewModel中的类似生命周期的观察
		 */
		lifecycle.addObserver(viewModel)
	}

	override fun initParam() {}

	/**
	 * 初始化根布局
	 *
	 * @return 布局layout的id
	 */
	abstract fun initContentView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): Int
	open fun initToolbar() {}

	/**
	 * 初始化ViewModel的id
	 *
	 * @return BR的id
	 */
	abstract fun initVariableId(): Int

	override fun initData() {}

	/**
	 * @param cls 类
	 * @param <T> 泛型参数,必须继承ViewMode
	 * @return 生成的viewMode实例
	</T> */
	private fun <T : ViewModel?> createViewModel(fragment: Fragment, cls: Class<T>): T {
		return ViewModelProvider(fragment)[cls]
	}

	//刷新布局数据
	fun refreshLayout() {
		binding.setVariable(viewModelId, viewModel)
	}

	override fun initViewObservable() {}
	override fun onDestroyView() {
		super.onDestroyView()
		if (binding != null) {
			binding.unbind()
		}
	}
}