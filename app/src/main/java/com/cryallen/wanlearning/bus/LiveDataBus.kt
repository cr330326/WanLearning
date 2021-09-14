package com.cryallen.wanlearning.bus

import androidx.lifecycle.*
import java.lang.Exception
import java.lang.NullPointerException

/***
 * 基于LiveData 扩展通信机制
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
object LiveDataBus {

	/**
	 * 粘性事件集合
	 */
	private var stickyBus: MutableMap<String, MutableLiveData<Any>>? = mutableMapOf()

	/**
	 * 普通事件结合
	 */
	private var bus: MutableMap<String, BusMutableLiveData<Any>>? = mutableMapOf()

	//传递普通事件
	fun with(key: String): MutableLiveData<Any?>? {
		return with(key, Any::class.java)
	}

	private fun <T> with(key: String, type: Class<T>?): BusMutableLiveData<T>? {
		if (!bus!!.containsKey(key)) {
			bus!![key] = BusMutableLiveData()
		}
		return bus!![key] as BusMutableLiveData<T>
	}

	//传递粘性事件
	fun withSticky(key: String): MutableLiveData<Any> {
		return withSticky(key, Any::class.java)
	}

	private fun <T> withSticky(key: String, type: Class<T>?): MutableLiveData<T> {
		if (!stickyBus!!.containsKey(key)) {
			stickyBus!![key] = MutableLiveData()
		}
		return stickyBus!![key] as MutableLiveData<T>
	}

	/***
	 *  扩展liveData hook源码拦截实现非粘性事件
	 ***/
	class BusMutableLiveData<T> : MutableLiveData<T?>() {
		private val observerMap: MutableMap<Observer<*>, Observer<*>> = mutableMapOf()

		fun observeSticky(
			owner: () -> Lifecycle,
			observer: (T?) -> Unit
		) {
			super.observe(owner, observer)
		}

		fun observeForeverSticky(observer: Observer<in T?>) {
			super.observeForever(observer)
		}

		override fun observe(owner: LifecycleOwner, observer: Observer<in T?>) {
			super.observe(owner, observer)
			try {
				hook(observer)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}

		override fun observeForever(observer: Observer<in T?>) {
			if (!observerMap.containsKey(observer)) {
				observerMap[observer] = ObserverWrapper(observer)
			}
			super.observeForever(observerMap[observer] as Observer<in T?>)
		}

		override fun removeObserver(observer: Observer<in T?>) {
			var realObserver: Observer<in T?> =
				if (observerMap.containsKey(observer)) {
					observerMap.remove(observer) as Observer<in T?>
				} else {
					observer
				}
			super.removeObserver(realObserver)
		}

		/**
		 * hook源码实现,拦截订阅之前的事件
		 * @param observer observer
		 */
		@Throws(Exception::class)
		private fun hook(observer: Observer<in T>) {
			//get wrapper's version
			val classLiveData = LiveData::class.java
			val fieldObservers = classLiveData.getDeclaredField("mObservers")
			fieldObservers.isAccessible = true
			val objectObservers = fieldObservers[this]
			val classObservers: Class<*> = objectObservers.javaClass
			val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
			methodGet.isAccessible = true
			val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
			var objectWrapper: Any? = null
			if (objectWrapperEntry is Map.Entry<*, *>) {
				objectWrapper = objectWrapperEntry.value
			}
			if (objectWrapper == null) {
				throw NullPointerException("Wrapper can not be bull!")
			}
			val classObserverWrapper: Class<*> = objectWrapper.javaClass.superclass
			val fieldLastVersion = classObserverWrapper.getDeclaredField("mLastVersion")
			fieldLastVersion.isAccessible = true
			//get livedata's version
			val fieldVersion = classLiveData.getDeclaredField("mVersion")
			fieldVersion.isAccessible = true
			val objectVersion = fieldVersion[this]
			//set wrapper's version
			fieldLastVersion[objectWrapper] = objectVersion
		}

	}

	class ObserverWrapper<T>(private val observer: Observer<T>?) : Observer<T> {
		override fun onChanged(t: T) {
			if (observer != null) {
				if (isCallOnObserve) {
					return
				}
				observer.onChanged(t)
			}
		}

		private val isCallOnObserve: Boolean
			private get() {
				val stackTrace = Thread.currentThread().stackTrace
				if (stackTrace != null && stackTrace.isNotEmpty()) {
					for (element in stackTrace) {
						if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
							return true
						}
					}
				}
				return false
			}

	}
}