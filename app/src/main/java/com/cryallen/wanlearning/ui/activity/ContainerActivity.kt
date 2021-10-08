package com.cryallen.wanlearning.ui.activity

import com.cryallen.wanlearning.base.BaseFragment

import android.os.Bundle
import android.content.Intent
import android.graphics.drawable.ColorDrawable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseActivity
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.databinding.ActivityContainerBinding

import java.lang.RuntimeException
import java.lang.ref.WeakReference


/***
 * 盛装Fragment的一个容器(代理)Activity
 * 普通界面只需要编写Fragment,使用此Activity盛装,这样就不需要每个界面都在AndroidManifest中注册一遍
 * @author vsh9p8q
 * @DATE 2021/9/24
 ***/
class ContainerActivity : BaseActivity() {

	private var _binding: ActivityContainerBinding? = null

	private val binding: ActivityContainerBinding get() = _binding!!

	private val fragmentManager: FragmentManager by lazy { supportFragmentManager }

	private var mWeakFragment: WeakReference<Fragment>? = null

	private var fragment: Fragment? = null

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		_binding = ActivityContainerBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (savedInstanceState != null) {
			fragment = fragmentManager.getFragment(savedInstanceState, CommonConfig.CONTAINER_FRAGMENT_TAG)
		}
		if (fragment == null) {
			fragment = initFromIntent(intent)
		}

		fragmentManager.beginTransaction().apply {
			replace(binding.containerContent.id, fragment!!)
		}.commitAllowingStateLoss()

		mWeakFragment = WeakReference(fragment)
	}

	override fun setupViews() {
		appViewModel.appColor.observe(this, Observer {
			supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
			setStatusBarBackground()
		})
	}

	override fun onDestroy() {
		super.onDestroy()
		_binding = null
	}

	override fun onSaveInstanceState(outState: Bundle) {
		super.onSaveInstanceState(outState)
		fragmentManager.putFragment(outState, CommonConfig.CONTAINER_FRAGMENT_TAG, mWeakFragment?.get()!!)
	}

	private fun initFromIntent(data: Intent?): Fragment? {
		if (data == null) {
			throw RuntimeException("you must provide a page info to display")
		}
		try {
			val fragmentName = data.getStringExtra(CommonConfig.CONTAINER_FRAGMENT)
			require(!(fragmentName == null || "" == fragmentName)) { "can not find page fragmentName" }
			val fragmentClass = Class.forName(fragmentName)
			val fragment: Fragment = fragmentClass.newInstance() as Fragment
			fragment.arguments = data.getBundleExtra(CommonConfig.CONTAINER_BUNDLE)
			return fragment

		} catch (e: ClassNotFoundException) {
			e.printStackTrace()
		} catch (e: InstantiationException) {
			e.printStackTrace()
		} catch (e: IllegalAccessException) {
			e.printStackTrace()
		}
		throw RuntimeException("fragment initialization failed!")
	}

	override fun onBackPressed() {
		val fragment: Fragment = fragmentManager.findFragmentById(binding.containerContent.id)!!
		if (fragment is BaseFragment) {
			if (!fragment.isBackPressed()) {
				super.onBackPressed()
			}
		} else {
			super.onBackPressed()
		}
	}
}