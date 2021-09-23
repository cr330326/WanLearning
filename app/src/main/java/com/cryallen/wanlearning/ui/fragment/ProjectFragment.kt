package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentProjectBinding
import com.cryallen.wanlearning.databinding.IncludeViewpagerBinding
import com.cryallen.wanlearning.exception.ExceptionHandle
import com.cryallen.wanlearning.ui.ext.*
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.ProjectViewModel

/***
 * 项目Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class ProjectFragment : BaseFragment(){

	private var _binding: FragmentProjectBinding? = null

	private val binding get() = _binding!!

	private lateinit var viewPagerBinding: IncludeViewpagerBinding

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getProjectViewModelFactory()).get(ProjectViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentProjectBinding.inflate(layoutInflater, container, false)
		viewPagerBinding = IncludeViewpagerBinding.bind(binding.root)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		//设置加载框附着点
		setLoadSir(viewPagerBinding.viewPager) {
			loadDataOnce()
		}

		//初始化viewpager2
		viewPagerBinding.viewPager.init(this,viewModel.fragments)
		//初始化 magic_indicator
		viewPagerBinding.magicIndicator.bindViewPager2(viewPagerBinding.viewPager,viewModel.titleList)
	}

	override fun createObserver() {
		//监听项目标题列表数据
		if (!viewModel.projectTitleLiveData.hasObservers()) {
			viewModel.projectTitleLiveData.observe(viewLifecycleOwner, Observer { result ->
				loadService.showSuccess()
				val response = result.getOrNull()
				if (response == null || !response.isSucces()) {
					//显示异常信息页面
					loadService.showError(ExceptionHandle.handleException(result.exceptionOrNull()).errorMsg)
					return@Observer
				}

				if (response.data.isNullOrEmpty()) {
					//显示空数据页面
					loadService.showEmpty()
					return@Observer
				}

				response.data.let {
					viewModel.titleList.clear()
					viewModel.titleList.addAll(it.map { it.name })
					it.forEach { classify ->
						viewModel.fragments.add(ProjectChildFragment.newInstance(classify.id))
					}
				}
				viewPagerBinding.magicIndicator.navigator.notifyDataSetChanged()
				viewPagerBinding.viewPager.adapter?.notifyDataSetChanged()
				viewPagerBinding.viewPager.offscreenPageLimit = viewModel.fragments.size
			})
		}
	}

	override fun loadDataOnce() {
		//设置界面 加载中
		loadService.showLoading()
		//请求标题数据
		viewModel.onProjectTitleRefresh()
	}

	companion object {

		fun newInstance() = ProjectFragment()
	}
}