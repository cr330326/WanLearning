package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentLoginBinding
import com.cryallen.wanlearning.ui.ext.initBack
import com.cryallen.wanlearning.ui.ext.showMessage
import com.cryallen.wanlearning.utils.CacheUtils
import com.cryallen.wanlearning.utils.GlobalUtils
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.LoginViewModel

/***
 * 登陆页面
 * @author vsh9p8q
 * @DATE 2021/9/25
 ***/
class LoginFragment : BaseFragment(){
	private var _binding: FragmentLoginBinding? = null

	private val binding get() = _binding!!

	private val loginHandler = Handler(Looper.getMainLooper()!!)

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getLoginViewModelFactory()).get(LoginViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		binding.viewModel = viewModel
		binding.click = ProxyClick()
		binding.loginToolbar.toolbar.initBack(GlobalUtils.getString(R.string.login_item_login)) {
			this.activity.finish()
		}

		//设置颜色跟主题颜色一致
		appViewModel.appColor.value?.let {
			GlobalUtils.setShapColor(binding.login, it)
			binding.loginGoregister.setTextColor(it)
			binding.loginToolbar.toolbar.setBackgroundColor(it)
		}
	}

	override fun createObserver() {
		//监听登陆成功后操作
		if (!viewModel.loginLiveData.hasObservers()) {
			viewModel.loginLiveData.observe(viewLifecycleOwner, Observer { result ->
				val response = result.getOrNull()
				if (response == null || !response.isSucces()) {
					showMessage(response!!.errorMsg)
					return@Observer
				}
				CacheUtils.setUser(response.data)
				CacheUtils.setIsLogin(true)

				appViewModel.userInfo.value = response.data

				loginHandler.postDelayed({
					this.activity.finish()
				},2000)
				showMessage(GlobalUtils.getString(R.string.login_success_tip))
			})
		}
	}

	companion object {
		fun newInstance() = MineFragment()
	}

	inner class ProxyClick {

		fun clickClear() {
			viewModel.username.set("")
		}

		/** 登陆 */
		fun clickLogin() {
			when {
				viewModel.username.get().isEmpty() -> showMessage(GlobalUtils.getString(R.string.login_item_input_username_tip))
				viewModel.password.get().isEmpty() -> showMessage(GlobalUtils.getString(R.string.login_item_input_password_tip))
				else -> viewModel.onLogin(viewModel.username.get(),viewModel.password.get())
			}
		}

		/** 注册 */
		fun clickRegister() {
			startContainerActivity(RegisterFragment::class.java.canonicalName)
		}

		var onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
			viewModel.isShowPwd.set(isChecked)
		}
	}
}