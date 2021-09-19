package com.cryallen.wanlearning.ui.ext

import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.ui.fragment.*
import com.cryallen.wanlearning.ui.view.loadCallBack.EmptyCallback
import com.cryallen.wanlearning.ui.view.loadCallBack.ErrorCallback
import com.cryallen.wanlearning.ui.view.loadCallBack.LoadingCallback
import com.cryallen.wanlearning.utils.GlobalUtil
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

/***
 * 自定义View 扩展文件
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
fun ViewPager2.initMain(fragment: Fragment): ViewPager2 {
	//是否可滑动
	this.isUserInputEnabled = false
	this.offscreenPageLimit = 5
	//设置适配器
	adapter = object : FragmentStateAdapter(fragment) {
		override fun createFragment(position: Int): Fragment {
			when (position) {
				0 -> {
					return HomeFragment()
				}
				1 -> {
					return WechatFragment()
				}
				2 -> {
					return ProjectFragment()
				}
				3 -> {
					return NavigationFragment()
				}
				4 -> {
					return MineFragment()
				}
				else -> {
					return HomeFragment()
				}
			}
		}
		override fun getItemCount() = 5
	}
	return this
}


/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
	setBackgroundColor(GlobalUtil.getThemeColor())
	title = titleStr
	return this
}

/**
 * 设置图片的颜色值
 */
fun ImageView.setIconTint(iconTint: ColorStateList? = null) {
	var iconDrawable = drawable
	if (iconDrawable != null) {
		iconDrawable = DrawableCompat.wrap(iconDrawable.constantState ?.newDrawable() ?: iconDrawable).mutate()
		if (iconTint != null) {
			DrawableCompat.setTintList(iconDrawable, iconTint)
			iconDrawable.invalidateSelf()
		}
	}
	setImageDrawable(iconDrawable)
}

fun LoadService<*>.setErrorText(message: String) {
	if (message.isNotEmpty()) {
		this.setCallBack(ErrorCallback::class.java) { _, view ->
			view.findViewById<TextView>(R.id.loading_error_tv).text = message
		}
	}
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
	this.setErrorText(message)
	this.showCallback(ErrorCallback::class.java)
}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
	this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
	this.showCallback(LoadingCallback::class.java)
}

/**
 * 设置loading的颜色 加载布局
 */
fun setLoadingColor(loadService: LoadService<Any>) {
	loadService.setCallBack(LoadingCallback::class.java) { _, view ->
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintList = ColorStateList.valueOf(GlobalUtil.getThemeColor())
		}
	}
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
	val loadService = LoadSir.getDefault().register(view) {
		//点击重试时触发的操作
		callback.invoke()
	}
	setLoadingColor(loadService)
	return loadService
}