package com.cryallen.wanlearning.ui.ext

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cryallen.wanlearning.ui.fragment.*

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