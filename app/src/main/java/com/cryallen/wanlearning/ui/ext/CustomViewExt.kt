package com.cryallen.wanlearning.ui.ext

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cryallen.wanlearning.ui.fragment.*
import com.cryallen.wanlearning.utils.GlobalUtil

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