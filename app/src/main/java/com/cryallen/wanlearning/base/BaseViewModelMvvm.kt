package com.cryallen.wanlearning.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/***
 * BaseViewModelMvvm 实现类
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
abstract class BaseViewModelMvvm : ViewModel(), IBaseViewModelMvvm, Consumer<Disposable?> {
	//管理RxJava，主要针对RxJava异步操作造成的内存泄漏
	private var mCompositeDisposable: CompositeDisposable?

	private fun addSubscribe(disposable: Disposable?) {
		if (mCompositeDisposable == null) {
			mCompositeDisposable = CompositeDisposable()
		}
		mCompositeDisposable!!.add(disposable!!)
	}

	override fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {}
	override fun onCreate() {}
	override fun onDestroy() {}
	override fun onStart() {}
	override fun onStop() {}
	override fun onResume() {}
	override fun onPause() {}
	override fun onCleared() {
		super.onCleared()
		//ViewModel销毁时会执行，同时取消所有异步任务
		if (mCompositeDisposable != null) {
			mCompositeDisposable!!.clear()
		}
	}

	@Throws(Exception::class)
	override fun accept(disposable: Disposable?) {
		addSubscribe(disposable)
	}

	init {
		mCompositeDisposable = CompositeDisposable()
	}

}