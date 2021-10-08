package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.IncludeListBinding
import com.cryallen.wanlearning.databinding.IncludeRefreshLayoutBinding
import com.cryallen.wanlearning.exception.ExceptionHandle
import com.cryallen.wanlearning.extension.dp2px
import com.cryallen.wanlearning.ui.adapter.NaviAdapter
import com.cryallen.wanlearning.ui.ext.*
import com.cryallen.wanlearning.ui.view.SpaceItemDecoration
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.TreeViewModel

/***
 * 导航Fragment
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class NaviFragment : BaseFragment(){

	private var _binding: IncludeListBinding? = null

	private val binding get() = _binding!!

	private lateinit var refreshLayoutBinding: IncludeRefreshLayoutBinding

	private val naviAdapter: NaviAdapter by lazy { NaviAdapter(viewModel.naviDataList) }

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getTreeViewModelFactory()).get(TreeViewModel::class.java) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = IncludeListBinding.inflate(layoutInflater, container, false)
		refreshLayoutBinding = IncludeRefreshLayoutBinding.bind(binding.root)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?) {
		//设置加载框附着点
		setLoadSir(refreshLayoutBinding.refreshLayout) {
			loadDataOnce()
		}
		refreshLayoutBinding.refreshLayout.setEnableRefresh(false)
		refreshLayoutBinding.refreshLayout.setEnableLoadMore(false)

		refreshLayoutBinding.recyclerView.init(LinearLayoutManager(context), naviAdapter).let {
			it.addItemDecoration(SpaceItemDecoration(0, dp2px(8f), false))
		}
	}

	override fun createObserver() {
		//监听导航列表数据
		if (!viewModel.naviLiveData.hasObservers()) {
			viewModel.naviLiveData.observe(viewLifecycleOwner, Observer { result ->
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

				viewModel.naviDataList.addAll(response.data)
				naviAdapter.notifyDataSetChanged()
			})
		}

		appViewModel.run {
			//监听全局的主题颜色改变
			appColor.observe(this@NaviFragment) {
				setUiTheme(it, loadService)
			}
		}
	}

	override fun loadDataOnce() {
		//设置界面 加载中
		loadService.showLoading()
		//请求导航数据
		viewModel.onNaviRefresh()
	}

	companion object {

		fun newInstance() = NaviFragment()
	}
}