package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentMineBinding
import com.cryallen.wanlearning.extension.loadCircle
import com.cryallen.wanlearning.ui.activity.WebViewActivity
import com.cryallen.wanlearning.ui.ext.notNull
import com.cryallen.wanlearning.ui.ext.setUiTheme
import com.cryallen.wanlearning.utils.CacheUtils
import com.cryallen.wanlearning.utils.GlobalUtils
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.MineViewModel

/***
 * 我的Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class MineFragment : BaseFragment(){

	private var _binding: FragmentMineBinding? = null

	private val binding get() = _binding!!

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getMineViewModelFactory()).get(MineViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentMineBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		binding.meImage.loadCircle(GlobalUtils.randomImage())
		binding.click = ProxyClick()

		appViewModel.appColor.value?.let {
			setUiTheme(it, binding.meLinear, binding.meIntegral)
		}

		appViewModel.userInfo.value?.let {
			if(it.nickname.isEmpty()) binding.meName.text = it.username else binding.meName.text = it.nickname
		}
	}

	override fun createObserver() {
		//个人积分数据
		if (!viewModel.integralLiveData.hasObservers()) {
			viewModel.integralLiveData.observe(viewLifecycleOwner, Observer { result ->
				val response = result.getOrNull()
				if (response == null || !response.isSucces()) {
					binding.meName.text = GlobalUtils.getString(R.string.mine_item_hint_login)
					binding.meInfo.text = GlobalUtils.getString(R.string.mine_item_hint_info)
					binding.meIntegral.visibility = View.GONE
					return@Observer
				}
				binding.meName.text = response.data.username
				binding.meInfo.text ="id：${response.data.userId}  排名：${response.data.rank}"
				binding.meIntegral.text = response.data.coinCount.toString()
				binding.meIntegral.visibility = View.VISIBLE
			})
		}

		appViewModel.run {
			appColor.observe(this@MineFragment, Observer {
				setUiTheme(it, binding.meLinear, binding.meIntegral)
			})

			userInfo.observe(this@MineFragment, Observer {
				it.notNull({
					if(it.nickname.isEmpty()) binding.meName.text = it.username else binding.meName.text = it.nickname
					loadDataOnce()
				},{
					binding.meName.text = GlobalUtils.getString(R.string.mine_item_hint_login)
					binding.meInfo.text = GlobalUtils.getString(R.string.mine_item_hint_info)
					binding.meIntegral.visibility = View.GONE
				})
			})
		}
	}

	override fun loadDataOnce() {
		viewModel.onMineRefresh()
	}

	inner class ProxyClick {

		/** 登陆 */
		fun clickLogin() {
			if(!CacheUtils.isLogin()){
				startContainerActivity(LoginFragment::class.java.canonicalName)
			}
		}

		/** 系统设置 */
		fun clickSetting() {
			startContainerActivity(SettingFragment::class.java.canonicalName)
		}

		/** 玩安卓 */
		fun clickWan() {
			WebViewActivity.start(activity,"玩Android网站","https://www.wanandroid.com/")
		}
	}

	companion object {

		fun newInstance() = MineFragment()
	}
}