package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentHomeBinding
import com.cryallen.wanlearning.databinding.IncludeRefreshLayoutBinding
import com.cryallen.wanlearning.exception.ExceptionHandle
import com.cryallen.wanlearning.extension.dp2px
import com.cryallen.wanlearning.ui.adapter.ArticleAdapter
import com.cryallen.wanlearning.ui.ext.*
import com.cryallen.wanlearning.utils.GlobalUtil
import com.cryallen.wanlearning.utils.LogUtils
import com.cryallen.wanlearning.viewmodel.HomeViewModel
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration

/***
 * 首页Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeFragment : BaseFragment(){

	private var _binding: FragmentHomeBinding? = null

	private lateinit var refreshLayoutBinding: IncludeRefreshLayoutBinding

	private val binding get() = _binding!!

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getHomeViewModelFactory()).get(HomeViewModel::class.java) }

	private val articleAdapter: ArticleAdapter by lazy { ArticleAdapter(viewModel.articleDataList) }

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		_binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
		refreshLayoutBinding = IncludeRefreshLayoutBinding.bind(binding.root)
		return super.onCreateView(binding.root)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	override fun initView(savedInstanceState: Bundle?){
		//初始化 toolbar
		binding.homeToolbar.toolbar.run {
			init(GlobalUtil.getString(R.string.menu_bottom_home))
			inflateMenu(R.menu.home_menu)
			setOnMenuItemClickListener {
				when (it.itemId) {
					R.id.home_search -> {
						//nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
					}
				}
				true
			}
		}
		//初始化横线
		binding.viewHorizontalLine.setBackgroundColor(GlobalUtil.getThemeColor())

		//初始化recyclerView
		refreshLayoutBinding.recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
			//因为首页要添加轮播图，所以我设置了firstNeedTop字段为false,即第一条数据不需要设置间距
			it.addItemDecoration(SpaceItemDecoration(0, 0, false))
			//初始化FloatingActionButton
			//it.initFloatBtn(floatbtn)
		}

		//设置加载框附着点
		setLoadSir(refreshLayoutBinding.refreshLayout) {
			//点击重试时触发的操作
			LogUtils.d(TAG,"点击重试")
			loadDataOnce()
			//mViewModel.getCollectAriticleData(true)
		}
	}

	//页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
	override fun createObserver(){
		//监听文章列表数据
		if (!viewModel.articleLiveData.hasObservers()) {
			viewModel.articleLiveData.observe(viewLifecycleOwner, Observer { result ->
				loadService.showSuccess()
				val response = result.getOrNull()
				if (response == null || !response.isSucces()) {
					//显示异常信息页面
					loadService.showError(ExceptionHandle.handleException(result.exceptionOrNull()).errorMsg)
					return@Observer
				}

				if (response.data.datas.isNullOrEmpty()) {
					//显示空数据页面
					loadService.showEmpty()
					return@Observer
				}

				viewModel.articleDataList.clear()
				viewModel.articleDataList.addAll(response.data.datas)
				val itemCount = articleAdapter.itemCount
				articleAdapter.notifyDataSetChanged()
				//articleAdapter.notifyItemRangeInserted(itemCount, response.data.datas.size)
				/*if (response.nextPageUrl.isNullOrEmpty()) {
					binding.refreshLayout.finishLoadMoreWithNoMoreData()
				} else {
					binding.refreshLayout.closeHeaderOrFooter()
				}*/
			})
		}
	}

	override fun loadDataOnce() {
		loadService.showLoading()
		viewModel.onRefresh()
	}

	companion object {

		fun newInstance() = HomeFragment()
	}
}