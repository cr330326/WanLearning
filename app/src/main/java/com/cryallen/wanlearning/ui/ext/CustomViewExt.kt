package com.cryallen.wanlearning.ui.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
 * 初始化有返回键的toolbar
 */
fun Toolbar.initBack(
	titleStr: String = "",
	backImg: Int = R.mipmap.ic_back,
	onBack: (toolbar: Toolbar) -> Unit
): Toolbar {
	setBackgroundColor(GlobalUtils.getThemeColor())
	title = titleStr.toHtml()
	setNavigationIcon(backImg)
	setNavigationOnClickListener { onBack.invoke(this) }
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
			view.findViewById<ProgressBar>(R.id.loading_progress).indeterminateTintMode = PorterDuff.Mode.SRC_ATOP
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

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showMessage(
	message: String,
	title: String = "温馨提示",
	positiveButtonText: String = "确定",
	positiveAction: () -> Unit = {},
	negativeButtonText: String = "",
	negativeAction: () -> Unit = {}
) {
	activity?.let {
		MaterialDialog(it)
			.cancelable(true)
			.lifecycleOwner(viewLifecycleOwner)
			.show {
				title(text = title)
				message(text = message)
				positiveButton(text = positiveButtonText) {
					positiveAction.invoke()
				}
				if (negativeButtonText.isNotEmpty()) {
					negativeButton(text = negativeButtonText) {
						negativeAction.invoke()
					}
				}
				getActionButton(WhichButton.POSITIVE).updateTextColor(GlobalUtils.getThemeColor())
				getActionButton(WhichButton.NEGATIVE).updateTextColor(GlobalUtils.getThemeColor())
			}
	}
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
fun AppCompatActivity.showMessage(
	message: String,
	title: String = "温馨提示",
	positiveButtonText: String = "确定",
	positiveAction: () -> Unit = {},
	negativeButtonText: String = "",
	negativeAction: () -> Unit = {}
) {
	MaterialDialog(this)
		.cancelable(true)
		.lifecycleOwner(this)
		.show {
			title(text = title)
			message(text = message)
			positiveButton(text = positiveButtonText) {
				positiveAction.invoke()
			}
			if (negativeButtonText.isNotEmpty()) {
				negativeButton(text = negativeButtonText) {
					negativeAction.invoke()
				}
			}
			getActionButton(WhichButton.POSITIVE).updateTextColor(GlobalUtils.getThemeColor())
			getActionButton(WhichButton.NEGATIVE).updateTextColor(GlobalUtils.getThemeColor())
		}
}

/**
 * 根据控件的类型设置主题，注意，控件具有优先级， 基本类型的控件建议放到最后，像 Textview，FragmentLayout，不然会出现问题，
 * 列如下面的BottomNavigationViewEx他的顶级父控件为FragmentLayout，如果先 is Fragmentlayout判断在 is BottomNavigationViewEx上面
 * 那么就会直接去执行 is FragmentLayout的代码块 跳过 is BottomNavigationViewEx的代码块了
 */
fun setUiTheme(color: Int, vararg anyList: Any?) {
	anyList.forEach { view ->
		view?.let {
			when (it) {
				is LoadService<*> -> setLoadingColor(it as LoadService<Any>)
				is Toolbar -> it.setBackgroundColor(color)
				is TextView -> it.setTextColor(color)
				is LinearLayout -> it.setBackgroundColor(color)
				is ConstraintLayout -> it.setBackgroundColor(color)
				is FrameLayout -> it.setBackgroundColor(color)
			}
		}
	}
}

/**
 * 判断是否为空 并传入相关操作
 */
inline fun <reified T> T?.notNull(notNullAction: (T) -> Unit, nullAction: () -> Unit = {}) {
	if (this != null) {
		notNullAction.invoke(this)
	} else {
		nullAction.invoke()
	}
}