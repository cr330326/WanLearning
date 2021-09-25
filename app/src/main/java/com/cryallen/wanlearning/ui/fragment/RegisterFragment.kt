package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentRegisterBinding
import com.cryallen.wanlearning.ui.ext.initBack
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

	}

	override fun createObserver() {

	}

	inner class ProxyClick {
		/** 注册 */
		fun clickRegister() {
			startContainerActivity(SettingFragment::class.java.canonicalName)
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