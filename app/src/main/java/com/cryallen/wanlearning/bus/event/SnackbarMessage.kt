package com.cryallen.wanlearning.bus.event

import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/***
 * A SingleLiveEvent used for Snackbar messages. Like a [SingleLiveEvent] but also prevents
 * null messages and uses a custom observer.
 *
 *
 * Note that only one observer is going to be notified of changes.
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
class SnackbarMessage : SingleLiveEvent<Int?>(){

	fun observe(owner: LifecycleOwner?, observer: SnackbarObserver) {
		super.observe(owner!!, Observer { t ->
			if (t == null) {
				return@Observer
			}
			observer.onNewMessage(t)
		})
	}

	interface SnackbarObserver {
		/**
		 * Called when there is a new message to be shown.
		 * @param snackbarMessageResourceId The new message, non-null.
		 */
		fun onNewMessage(@StringRes snackbarMessageResourceId: Int)
	}
}