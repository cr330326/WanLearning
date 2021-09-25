package com.cryallen.wanlearning.ui.databind

import androidx.databinding.ObservableField

/***
 * 自定义的String类型 ObservableField  提供了默认值，避免取值的时候还要判空
 ***/
open class StringObservableField(value: String = "") : ObservableField<String>(value) {

	override fun get(): String {
		return super.get()!!
	}
}

/***
 * 自定义的Boolean类型ObservableField 提供了默认值，避免取值的时候还要判空
 ***/
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
	override fun get(): Boolean {
		return super.get()!!
	}

}