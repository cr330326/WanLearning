package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentMineBinding

/***
 * 我的Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class MineFragment : BaseFragment(){

	private var _binding: FragmentMineBinding? = null

	private val binding
		get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentMineBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {}

	override fun createObserver() {}

	companion object {

		fun newInstance() = MineFragment()
	}
}