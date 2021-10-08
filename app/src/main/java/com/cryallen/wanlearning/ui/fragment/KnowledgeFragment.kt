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
import com.cryallen.wanlearning.ui.adapter.KnowledgeAdapter
import com.cryallen.wanlearning.ui.ext.*
import com.cryallen.wanlearning.ui.view.SpaceItemDecoration
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.TreeViewModel

/***
 * 体系Fragment
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class KnowledgeFragment : BaseFragment(){

	private var _binding: IncludeListBinding? = null

	private val binding get() = _binding!!

	private lateinit var refreshLayoutBinding: IncludeRefreshLayoutBinding

	private val knowAdapter: KnowledgeAdapter by lazy { KnowledgeAdapter(viewModel.knowledgeList) }

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

		refreshLayoutBinding.recyclerView.init(LinearLayoutManager(context), knowAdapter).let {
			it.addItemDecoration(SpaceItemDecoration(0, dp2px(8f), false))
		}
	}

	override fun createObserver() {
		//监听知识体系列表数据
		if (!viewModel.knowledgeLiveData.hasObservers()) {
			viewModel.knowledgeLiveData.observe(viewLifecycleOwner, Observer { result ->
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

				viewModel.knowledgeList.addAll(response.data)
				knowAdapter.notifyDataSetChanged()
			})
		}

		appViewModel.run {
			//监听全局的主题颜色改变
			appColor.observe(this@KnowledgeFragment) {
				setUiTheme(it, loadService)
			}
		}
	}

	override fun loadDataOnce() {
		//设置界面 加载中
		loadService.showLoading()
		//请求知识体系数据
		viewModel.onKnowledgeRefresh()
	}

	companion object {

		fun newInstance() = KnowledgeFragment()
	}
}