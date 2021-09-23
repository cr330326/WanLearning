package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.IncludeListBinding
import com.cryallen.wanlearning.databinding.IncludeRefreshLayoutBinding
import com.cryallen.wanlearning.exception.ExceptionHandle
import com.cryallen.wanlearning.extension.dp2px
import com.cryallen.wanlearning.ui.adapter.ArticleAdapter
import com.cryallen.wanlearning.ui.ext.init
import com.cryallen.wanlearning.ui.ext.showEmpty
import com.cryallen.wanlearning.ui.ext.showError
import com.cryallen.wanlearning.ui.ext.showLoading
import com.cryallen.wanlearning.ui.view.SpaceItemDecoration
import com.cryallen.wanlearning.utils.LogUtils
import com.cryallen.wanlearning.viewmodel.InjectorProvider
import com.cryallen.wanlearning.viewmodel.WechatViewModel

/***
 * 公众号章节Fragment
 * @author vsh9p8q
 * @DATE 2021/9/22
 ***/
class WechatChapterFragment : BaseFragment() {

	private var _binding: IncludeListBinding? = null

	private val binding get() = _binding!!

	//该项目对应的id
	private var cid = 0

	private var isNoLoadMore : Boolean = false

	private lateinit var refreshLayoutBinding: IncludeRefreshLayoutBinding

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getWechatViewModelFactory()).get(WechatViewModel::class.java) }

	private val articleAdapter: ArticleAdapter by lazy { ArticleAdapter(viewModel.articleDataList) }

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
			LogUtils.d(TAG,"点击刷新ViewPager")
			loadDataOnce()
		}

		arguments?.let {
			cid = it.getInt("cid")
		}

		//初始化recyclerView
		refreshLayoutBinding.recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
			//因为首页要添加轮播图，所以我设置了firstNeedTop字段为false,即第一条数据不需要设置间距
			it.addItemDecoration(SpaceItemDecoration(0, dp2px(8f), false))
		}

		//构建refreshLayout
		refreshLayoutBinding.refreshLayout.run {
			setOnRefreshListener {
				finishRefresh()
				viewModel.onArticleRefresh(true,cid)
			}
			setOnLoadMoreListener {
				if(!isNoLoadMore){
					finishLoadMore()
					viewModel.onArticleRefresh(false,cid)
				}else{
					finishLoadMoreWithNoMoreData()
				}
			}
		}
	}

	override fun createObserver() {
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

				when(viewModel.pageNo) {
					0 -> {
						isNoLoadMore = false
						viewModel.articleDataList.clear()
						viewModel.pageNo++
					}
					response.data.pageCount -> {
						//关闭加载完成
						isNoLoadMore = true
						refreshLayoutBinding.refreshLayout.finishLoadMoreWithNoMoreData()
					}
					else -> {
						viewModel.pageNo++
					}
				}
				viewModel.articleDataList.addAll(response.data.datas)
				articleAdapter.notifyDataSetChanged()
			})
		}
	}

	override fun loadDataOnce() {
		//设置界面 加载中
		loadService.showLoading()
		//请求标题数据
		viewModel.onArticleRefresh(true,cid)
	}

	companion object {

		fun newInstance(cid: Int): WechatChapterFragment {
			val args = Bundle()
			args.putInt("cid", cid)
			val fragment = WechatChapterFragment()
			fragment.arguments = args
			return fragment
		}
	}


}