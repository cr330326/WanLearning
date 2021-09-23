package com.cryallen.wanlearning.ui.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.WanApplication
import com.cryallen.wanlearning.extension.toHtml
import com.cryallen.wanlearning.ui.fragment.*
import com.cryallen.wanlearning.ui.view.indicator.utils.IndicatorUtils
import com.cryallen.wanlearning.ui.view.loadcallback.EmptyCallback
import com.cryallen.wanlearning.ui.view.loadcallback.ErrorCallback
import com.cryallen.wanlearning.ui.view.loadcallback.LoadingCallback
import com.cryallen.wanlearning.ui.view.magicindicator.MagicIndicator
import com.cryallen.wanlearning.ui.view.magicindicator.buildins.commonnavigator.CommonNavigator
import com.cryallen.wanlearning.ui.view.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.cryallen.wanlearning.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.cryallen.wanlearning.ui.view.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.cryallen.wanlearning.ui.view.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import com.cryallen.wanlearning.ui.view.viewpager.ScaleTransitionPagerTitleView
import com.cryallen.wanlearning.utils.GlobalUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


/***
 * 自定义View 扩展文件
 ***/
fun ViewPager2.init(
	fragment: Fragment,
	fragments: ArrayList<Fragment>,
	isUserInputEnabled: Boolean = true
): ViewPager2 {
	//是否可滑动
	this.isUserInputEnabled = isUserInputEnabled
	//设置适配器
	adapter = object : FragmentStateAdapter(fragment) {
		override fun createFragment(position: Int) = fragments[position]
		override fun getItemCount() = fragments.size
	}
	return this
}

/***
 * MagicIndicator指示器绑定ViewPager2
 ***/
fun MagicIndicator.bindViewPager2(
	viewPager: ViewPager2,
	mStringList: List<String> = arrayListOf(),
	action: (index: Int) -> Unit = {}) {
	val commonNavigator = CommonNavigator(WanApplication.instance)
	commonNavigator.adapter = object : CommonNavigatorAdapter() {

		override fun getCount(): Int {
			return  mStringList.size
		}
		override fun getTitleView(context: Context, index: Int): IPagerTitleView {
			return ScaleTransitionPagerTitleView(WanApplication.instance).apply {
				//设置文本
				text = mStringList[index].toHtml()
				//字体大小
				textSize = 17f
				//未选中颜色
				normalColor = Color.GRAY
				//选中颜色
				selectedColor = Color.WHITE
				//点击事件
				setOnClickListener {
					viewPager.currentItem = index
					action.invoke(index)
				}
			}
		}
		override fun getIndicator(context: Context): IPagerIndicator {
			return LinePagerIndicator(context).apply {
				mode = LinePagerIndicator.MODE_EXACTLY
				//线条的宽高度
				lineHeight = IndicatorUtils.dp2px(3.0f).toFloat()
				lineWidth = IndicatorUtils.dp2px(30.0f).toFloat()
				//线条的圆角
				roundRadius = IndicatorUtils.dp2px(6.0f).toFloat()
				startInterpolator = AccelerateInterpolator()
				endInterpolator = DecelerateInterpolator(2.0f)
				//线条的颜色
				setColors(Color.WHITE)
			}
		}
	}
	this.navigator = commonNavigator

	viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
		override fun onPageSelected(position: Int) {
			super.onPageSelected(position)
			this@bindViewPager2.onPageSelected(position)
			action.invoke(position)
		}

		override fun onPageScrolled(
			position: Int,
			positionOffset: Float,
			positionOffsetPixels: Int
		) {
			super.onPageScrolled(position, positionOffset, positionOffsetPixels)
			this@bindViewPager2.onPageScrolled(position, positionOffset, positionOffsetPixels)
		}

		override fun onPageScrollStateChanged(state: Int) {
			super.onPageScrollStateChanged(state)
			this@bindViewPager2.onPageScrollStateChanged(state)
		}
	})
}

/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
	setBackgroundColor(GlobalUtils.getThemeColor())
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
			view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintList = ColorStateList.valueOf(GlobalUtils.getThemeColor())
		}
	}
}

/**
 * loadService初始化
 */
fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
	val loadService = LoadSir.getDefault().register(view) {
		//点击重试时触发的操作
		callback.invoke()
	}
	setLoadingColor(loadService)
	return loadService
}

//绑定RecyclerView
fun RecyclerView.init(
	layoutManger: RecyclerView.LayoutManager,
	bindAdapter: RecyclerView.Adapter<*>,
	isScroll: Boolean = true
): RecyclerView {
	layoutManager = layoutManger
	setHasFixedSize(true)
	adapter = bindAdapter
	itemAnimator = null
	isNestedScrollingEnabled = isScroll
	return this
}