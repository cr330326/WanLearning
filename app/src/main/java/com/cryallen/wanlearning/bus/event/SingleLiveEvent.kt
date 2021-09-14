package com.cryallen.wanlearning.bus.event

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cryallen.wanlearning.utils.LogUtils
import java.util.concurrent.atomic.AtomicBoolean

/***
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update
 * can be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 *
 * Note that only one observer is going to be notified of changes.
 * SingleLiveEvent 的问题在于它仅限于一个观察者
 * SingleLiveEvent 类是为了适用于特定场景的解决方法。这是一个只会发送一次更新的 LiveData。
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
open class SingleLiveEvent<T> : MutableLiveData<T?>() {
	private val mPending = AtomicBoolean(false)

	//  @MainThread
	//    public void observe(LifecycleOwner owner, final Observer<T> observer) {
	@MainThread
	fun mObserve(owner: LifecycleOwner?, observer: Observer<T?>) {
		if (hasActiveObservers()) {
			LogUtils.w(TAG, "Multiple observers registered but only one will be notified of changes.")
		}

		// Observe the internal MutableLiveData
		super.observe(owner!!, Observer { t ->
			if (mPending.compareAndSet(true, false)) {
				observer.onChanged(t)
			}
		})
	}

	@MainThread
	override fun setValue(t: T?) {
		mPending.set(true)
		super.setValue(t)
	}

	/**
	 * Used for cases where T is Void, to make calls cleaner.
	 */
	@MainThread
	fun call() {
		value = null
	}

	companion object {
		private const val TAG = "SingleLiveEvent"
	}
}