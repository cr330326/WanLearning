package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentHomeBinding
import com.cryallen.wanlearning.utils.GlobalUtil

/***
 * 首页Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeFragment : BaseFragment(){

	private var _binding: FragmentHomeBinding? = null

	private val binding
		get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupViews()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	fun setupViews(){
		binding.viewHorizontalLine.setBackgroundColor(GlobalUtil.getThemeColor())
	}

	companion object {

		fun newInstance() = HomeFragment()
	}
}