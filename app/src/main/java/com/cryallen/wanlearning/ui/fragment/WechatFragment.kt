package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentWechatBinding
import com.cryallen.wanlearning.databinding.IncludeViewpagerBinding
import com.cryallen.wanlearning.exception.ExceptionHandle
import com.cryallen.wanlearning.ui.ext.*
import com.cryallen.wanlearning.utils.LogUtils
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.WechatViewModel

/***
 * 公众号Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class WechatFragment : BaseFragment(){

	private var _binding: FragmentWechatBinding? = null

	private val binding
		get() = _binding!!

	private lateinit var viewPagerBinding: IncludeViewpagerBinding

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getWechatViewModelFactory()).get(WechatViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentWechatBinding.inflate(layoutInflater, container, false)
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
			LogUtils.d(TAG,"点击刷新Title数据")
			loadDataOnce()
		}

		//初始化viewpager2
		viewPagerBinding.viewPager.init(this,viewModel.fragments)
		//初始化 magic_indicator
		viewPagerBinding.magicIndicator.bindViewPager2(viewPagerBinding.viewPager,viewModel.titleList)
	}

	override fun createObserver() {
		//监听微信标题列表数据
		if (!viewModel.wxChapterTitleLiveData.hasObservers()) {
			viewModel.wxChapterTitleLiveData.observe(viewLifecycleOwner, Observer { result ->
				loadService.showSuccess()
				val response = result.getOrNull()
				LogUtils.d(TAG,"createObserver response:" + response)
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
						viewModel.fragments.add(WechatChapterFragment.newInstance(classify.id))
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
		viewModel.onWxChapterTitleRefresh()
	}

	companion object {

		fun newInstance() = WechatFragment()
	}
}