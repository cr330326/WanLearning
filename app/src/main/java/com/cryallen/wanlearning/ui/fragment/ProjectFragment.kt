package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentProjectBinding

/***
 * 项目Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class ProjectFragment : BaseFragment(){

	private var _binding: FragmentProjectBinding? = null

	private val binding
		get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentProjectBinding.inflate(layoutInflater, container, false)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	companion object {

		fun newInstance() = ProjectFragment()
	}
}