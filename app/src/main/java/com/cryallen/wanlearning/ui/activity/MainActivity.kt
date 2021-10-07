package com.cryallen.wanlearning.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.base.BaseActivity
import com.cryallen.wanlearning.databinding.ActivityMainBinding
import com.cryallen.wanlearning.extension.setOnClickListener
import com.cryallen.wanlearning.extension.showToast
import com.cryallen.wanlearning.ui.ext.setIconTint
import com.cryallen.wanlearning.ui.fragment.*
import com.cryallen.wanlearning.utils.ActivityCollector
import com.cryallen.wanlearning.utils.GlobalUtils
import com.permissionx.guolindev.PermissionX

/***
 * 应用首页
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
class MainActivity : BaseActivity() {
	private var _binding: ActivityMainBinding? = null

	private val binding: ActivityMainBinding
		get() = _binding!!

	private var backPressTime = 0L

	private var homeFragment: HomeFragment? = null

	private var wechatFragment: WechatFragment? = null

	private var projectFragment: ProjectFragment? = null

	private var treeArrFragment: TreeArrFragment? = null

	private var mineFragment: MineFragment? = null

	private val fragmentManager: FragmentManager by lazy { supportFragmentManager }

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		requestWriteExternalStoragePermission()
		_binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	override fun onBackPressed() {
		if (supportFragmentManager.backStackEntryCount > 0) {
			supportFragmentManager.popBackStack()
		} else {
			processBackPressed()
		}
	}

	private fun processBackPressed() {
		val now = System.currentTimeMillis()
		if (now - backPressTime > 2000) {
			String.format(GlobalUtils.getString(R.string.press_again_to_exit), GlobalUtils.appName).showToast()
			backPressTime = now
		} else {
			ActivityCollector.appExit()
		}
	}

	override fun setupViews() {
		super.setupViews()
		binding.bottomLine.setBackgroundColor(GlobalUtils.getThemeColor())
		setOnClickListener(
			binding.navigationBar.btnMenuHome, binding.navigationBar.btnMenuWechat, binding.navigationBar.btnMenuProject, binding.navigationBar.btnMenuNavi,
			binding.navigationBar.btnMenuMine
		) {
			when (this) {
				binding.navigationBar.btnMenuHome -> {
					setTabSelection(0)
				}
				binding.navigationBar.btnMenuWechat -> {
					setTabSelection(1)
				}
				binding.navigationBar.btnMenuProject -> {
					setTabSelection(2)
				}
				binding.navigationBar.btnMenuNavi -> {
					setTabSelection(3)
				}
				binding.navigationBar.btnMenuMine -> {
					setTabSelection(4)
				}
			}
		}
		setTabSelection(0)
	}

	private fun setTabSelection(index: Int) {
		clearAllSelected()
		fragmentManager.beginTransaction().apply {
			hideFragments(this)
			when (index) {
				0 -> {
					binding.navigationBar.ivMenuHome.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuHome.setTextColor(GlobalUtils.getThemeColor())
					if (homeFragment == null) {
						homeFragment = HomeFragment.newInstance()
						add(binding.mainContainer.id, homeFragment!!)
					} else {
						show(homeFragment!!)
					}
				}
				1 -> {
					binding.navigationBar.ivMenuWechat.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuWechat.setTextColor(GlobalUtils.getThemeColor())
					if (wechatFragment == null) {
						wechatFragment = WechatFragment.newInstance()
						add(binding.mainContainer.id, wechatFragment!!)
					} else {
						show(wechatFragment!!)
					}
				}
				2 -> {
					binding.navigationBar.ivMenuProject.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuProject.setTextColor(GlobalUtils.getThemeColor())
					if (projectFragment == null) {
						projectFragment = ProjectFragment.newInstance()
						add(binding.mainContainer.id, projectFragment!!)
					} else {
						show(projectFragment!!)
					}
				}
				3 -> {
					binding.navigationBar.ivMenuNavi.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuNavi.setTextColor(GlobalUtils.getThemeColor())
					if (treeArrFragment == null) {
						treeArrFragment = TreeArrFragment.newInstance()
						add(binding.mainContainer.id, treeArrFragment!!)
					} else {
						show(treeArrFragment!!)
					}
				}
				4 -> {
					binding.navigationBar.ivMenuMine.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuMine.setTextColor(GlobalUtils.getThemeColor())
					if (mineFragment == null) {
						mineFragment = MineFragment.newInstance()
						add(binding.mainContainer.id, mineFragment!!)
					} else {
						show(mineFragment!!)
					}
				}
				else -> {
					binding.navigationBar.ivMenuHome.setIconTint(ColorStateList.valueOf(GlobalUtils.getThemeColor()))
					binding.navigationBar.tvMenuHome.setTextColor(GlobalUtils.getThemeColor())
					if (homeFragment == null) {
						homeFragment = HomeFragment.newInstance()
						add(binding.mainContainer.id, homeFragment!!)
					} else {
						show(homeFragment!!)
					}
				}
			}
		}.commitAllowingStateLoss()
	}

	private fun clearAllSelected() {
		//首页
		binding.navigationBar.ivMenuHome.setIconTint(ColorStateList.valueOf(GlobalUtils.getColor(R.color.color_default)))
		binding.navigationBar.tvMenuHome.setTextColor(GlobalUtils.getColor(R.color.color_default))
		//公众号
		binding.navigationBar.ivMenuWechat.setIconTint(ColorStateList.valueOf(GlobalUtils.getColor(R.color.color_default)))
		binding.navigationBar.tvMenuWechat.setTextColor(GlobalUtils.getColor(R.color.color_default))
		//项目
		binding.navigationBar.ivMenuProject.setIconTint(ColorStateList.valueOf(GlobalUtils.getColor(R.color.color_default)))
		binding.navigationBar.tvMenuProject.setTextColor(GlobalUtils.getColor(R.color.color_default))
		//导航
		binding.navigationBar.ivMenuNavi.setIconTint(ColorStateList.valueOf(GlobalUtils.getColor(R.color.color_default)))
		binding.navigationBar.tvMenuNavi.setTextColor(GlobalUtils.getColor(R.color.color_default))
		//我的
		binding.navigationBar.ivMenuMine.setIconTint(ColorStateList.valueOf(GlobalUtils.getColor(R.color.color_default)))
		binding.navigationBar.tvMenuMine.setTextColor(GlobalUtils.getColor(R.color.color_default))
	}

	private fun hideFragments(transaction: FragmentTransaction) {
		transaction.run {
			if (homeFragment != null) hide(homeFragment!!)
			if (wechatFragment != null) hide(wechatFragment!!)
			if (projectFragment != null) hide(projectFragment!!)
			if (treeArrFragment != null) hide(treeArrFragment!!)
			if (mineFragment != null) hide(mineFragment!!)
		}
	}

	private fun requestWriteExternalStoragePermission() {
		PermissionX.init(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			.onExplainRequestReason { scope, deniedList ->
				val message = GlobalUtils.getString(R.string.request_permission_picture_processing)
				scope.showRequestReasonDialog(deniedList, message, GlobalUtils.getString(R.string.dialog_confirm), GlobalUtils.getString(R.string.dialog_cancel))
			}
			.onForwardToSettings { scope, deniedList ->
				val message = GlobalUtils.getString(R.string.request_permission_picture_processing)
				scope.showForwardToSettingsDialog(deniedList, message, GlobalUtils.getString(R.string.dialog_settings), GlobalUtils.getString(R.string.dialog_cancel))
			}
			.request { allGranted, grantedList, deniedList ->
				if(!allGranted){
					GlobalUtils.getString(R.string.request_permission_error_tip).showToast()
				}
			}
	}

	companion object {

		fun start(context: Context) {
			context.startActivity(Intent(context, MainActivity::class.java))
		}
	}
}