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
import com.cryallen.wanlearning.databinding.FragmentRegisterBinding
import com.cryallen.wanlearning.ui.ext.initBack
import com.cryallen.wanlearning.ui.ext.showMessage
import com.cryallen.wanlearning.utils.CacheUtils
import com.cryallen.wanlearning.utils.GlobalUtils
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.LoginViewModel

/***
 * 注册页面
 * @author vsh9p8q
 * @DATE 2021/9/25
 ***/
class RegisterFragment : BaseFragment(){
	private var _binding: FragmentRegisterBinding? = null

	private val binding get() = _binding!!

	private val registerHandler = Handler(Looper.getMainLooper()!!)

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getLoginViewModelFactory()).get(LoginViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		binding.viewModel = viewModel
		binding.click = ProxyClick()
		binding.registerToolbar.toolbar.initBack(GlobalUtils.getString(R.string.login_item_register)) {
			this.activity.finish()
		}

		//设置颜色跟主题颜色一致
		appViewModel.appColor.value?.let {
			GlobalUtils.setShapColor(binding.registerAndLogin, it)
			binding.registerToolbar.toolbar.setBackgroundColor(it)
		}
	}

	override fun createObserver() {
		//监听注册成功后操作
		if (!viewModel.registerAndLoginLiveData.hasObservers()) {
			viewModel.registerAndLoginLiveData.observe(viewLifecycleOwner, Observer { result ->
				val response = result.getOrNull()
				if (response == null || !response.isSucces()) {
					showMessage(response!!.errorMsg)
					return@Observer
				}

				CacheUtils.setUser(response.data)
				CacheUtils.setIsLogin(true)

				appViewModel.userInfo.value = response.data

				registerHandler.postDelayed({
					this.activity.finish()
				},2000)
				showMessage(GlobalUtils.getString(R.string.login_success_tip))
			})
		}
	}

	inner class ProxyClick {

		/**清空*/
		fun clickClear() {
			viewModel.username.set("")
		}

		/** 注册 */
		fun clickRegister() {
			when {
				viewModel.username.get().isEmpty() -> showMessage("请填写账号")
				viewModel.password.get().isEmpty() -> showMessage("请填写密码")
				viewModel.confirmPassword.get().isEmpty() -> showMessage("请填写确认密码")
				viewModel.password.get().length < 6 -> showMessage("密码最少6位")
				viewModel.password.get() != viewModel.confirmPassword.get() -> showMessage("密码不一致")
				else -> viewModel.onRegister(viewModel.username.get(),viewModel.password.get(),viewModel.confirmPassword.get())
			}
		}

		var onCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
				viewModel.isShowPwd.set(isChecked)
		}

		var onConfirmCheckedChangeListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
			viewModel.isConfirmShowPwd.set(isChecked)
		}
	}

	companion object {

		fun newInstance() = MineFragment()
	}
}