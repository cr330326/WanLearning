package com.cryallen.wanlearning.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.color.colorChooser
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.ui.ext.initBack
import com.cryallen.wanlearning.ui.ext.showMessage
import com.cryallen.wanlearning.ui.view.preference.CheckBoxPreference
import com.cryallen.wanlearning.ui.view.preference.IconPreference
import com.cryallen.wanlearning.ui.view.preference.PreferenceCategory
import com.cryallen.wanlearning.utils.CacheUtils
import com.cryallen.wanlearning.utils.ColorUtils
import com.cryallen.wanlearning.utils.GlobalUtils

/***
 * 系统设置Fragment
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener{

	private var colorPreview: IconPreference? = null

	var toolbarView: View? = null
	var containerView: FrameLayout? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//这里重写根据PreferenceFragmentCompat 的布局 ，往他的根布局插入了一个toolbar
		containerView = view.findViewById<FrameLayout>(android.R.id.list_container)
		containerView?.let {
			//转为线性布局
			val linearLayout = it.parent as? LinearLayout
			linearLayout?.run {
				toolbarView = LayoutInflater.from(context).inflate(R.layout.include_toolbar, null)
				toolbarView?.let {view ->
					view.findViewById<Toolbar>(R.id.toolbar)?.initBack(GlobalUtils.getString(R.string.menu_top_setting)) {
						//关闭当前页面
						this@SettingFragment.activity?.finish()
					}
					//添加到第一个
					addView(toolbarView, 0)
				}
			}
		}
	}

	override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
		addPreferencesFromResource(R.xml.root_preferences)
		colorPreview = findPreference("color")
		initText()
		findPreference<Preference>("exit")?.isVisible = CacheUtils.isLogin()//未登录时，退出登录需要隐藏

		findPreference<Preference>("exit")?.setOnPreferenceClickListener { preference ->
			showMessage(
				GlobalUtils.getString(R.string.setting_login_out_tip),
				positiveButtonText = GlobalUtils.getString(R.string.dialog_exit),
				negativeButtonText = GlobalUtils.getString(R.string.dialog_cancel),
				positiveAction = {
					//清空账号
					CacheUtils.setUser(null)
					//appViewModel.userInfo.value = null
				})
			false
		}

		findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
			showMessage(
				GlobalUtils.getString(R.string.setting_clear_tip),
				positiveButtonText = GlobalUtils.getString(R.string.dialog_clear),
				negativeButtonText = GlobalUtils.getString(R.string.dialog_cancel),
				positiveAction = {
					activity?.let {
						CacheUtils.clearAllCache(it as? AppCompatActivity)
					}
					initText()
				})
			false
		}

		findPreference<IconPreference>("color")?.setOnPreferenceClickListener {
			activity?.let { activity ->
				MaterialDialog(activity).show {
					title(R.string.choose_theme_color)
					colorChooser(
						ColorUtils.ACCENT_COLORS,
						initialSelection = GlobalUtils.getThemeColor(),
						subColors = ColorUtils.PRIMARY_COLORS_SUB
					) { dialog, color ->
						///修改颜色
						GlobalUtils.setThemeColor(color)
						findPreference<PreferenceCategory>("base")?.setTitleColor(color)
						findPreference<PreferenceCategory>("other")?.setTitleColor(color)
						findPreference<PreferenceCategory>("about")?.setTitleColor(color)
						findPreference<CheckBoxPreference>("top")?.setBottonColor()
						toolbarView?.setBackgroundColor(color)
						//通知其他界面立马修改配置
						//appViewModel.appColor.value = color
					}
					getActionButton(WhichButton.POSITIVE).updateTextColor(
						GlobalUtils.getThemeColor()
					)
					getActionButton(WhichButton.NEGATIVE).updateTextColor(
						GlobalUtils.getThemeColor()
					)
					positiveButton(R.string.dialog_confirm)
					negativeButton(R.string.dialog_cancel)
				}
			}
			false
		}

		findPreference<Preference>("version")?.setOnPreferenceClickListener {
			//Beta.checkUpgrade(true, false)
			false
		}

		findPreference<Preference>("copyRight")?.setOnPreferenceClickListener {
			activity?.let {
				showMessage(it.getString(R.string.copyright_tip))
			}
			false
		}

		findPreference<Preference>("author")?.setOnPreferenceClickListener {
			false
		}

		findPreference<Preference>("project")?.setOnPreferenceClickListener {
			false
		}
	}

	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
		if (key == "color") {
			colorPreview?.setView()
		}
		if (key == "top") {
			CacheUtils.setIsNeedTop(sharedPreferences!!.getBoolean("top", true))
		}
	}

	override fun onResume() {
		super.onResume()
		preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onPause() {
		super.onPause()
		preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		containerView?.removeAllViews()
		toolbarView = null
	}

	/**
	 * 初始化设值
	 */
	private fun initText() {
		activity?.let {
			findPreference<CheckBoxPreference>("top")?.isChecked = CacheUtils.isNeedTop()
			findPreference<Preference>("clearCache")?.summary = CacheUtils.getTotalCacheSize(it)
			findPreference<Preference>("version")?.summary = "当前版本 " + GlobalUtils.appVersionName
		}
	}
}