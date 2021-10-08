package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentTreeBinding
import com.cryallen.wanlearning.databinding.IncludeViewpagerBinding
import com.cryallen.wanlearning.ui.ext.bindViewPager2
import com.cryallen.wanlearning.ui.ext.init
import com.cryallen.wanlearning.ui.ext.setUiTheme

/***
 * 聚合Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class TreeArrFragment : BaseFragment() {

	private var _binding: FragmentTreeBinding? = null

	private val binding get() = _binding!!

	//fragment集合
	private var fragments: ArrayList<Fragment> = arrayListOf()

	//标题集合
	private var titleList: ArrayList<String> = arrayListOf("体系", "导航")

	private lateinit var viewPagerBinding: IncludeViewpagerBinding

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentTreeBinding.inflate(layoutInflater, container, false)
		viewPagerBinding = IncludeViewpagerBinding.bind(binding.root)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		//初始化viewpager2
		viewPagerBinding.viewPager.init(this,fragments)
		//初始化 magic_indicator
		viewPagerBinding.magicIndicator.bindViewPager2(viewPagerBinding.viewPager,titleList)
		appViewModel.appColor.value?.let { setUiTheme(it, viewPagerBinding.viewpagerLinear) }
	}

	override fun createObserver() {
		appViewModel.run {
			//监听全局的主题颜色改变
			appColor.observe(this@TreeArrFragment) {
				setUiTheme(it, viewPagerBinding.viewpagerLinear)
			}
		}
	}

	companion object {

		fun newInstance() = TreeArrFragment()
	}

	init {
		fragments.add(KnowledgeFragment())
		fragments.add(NaviFragment())
	}
}