package com.cryallen.wanlearning.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.base.BaseFragment
import com.cryallen.wanlearning.databinding.FragmentHomeBinding
import com.cryallen.wanlearning.ui.ext.init
import com.cryallen.wanlearning.utils.GlobalUtil
import com.cryallen.wanlearning.viewmodel.HomeViewModel
import com.cryallen.wanlearning.viewmodel.InjectorProvider

/***
 * 首页Fragment
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeFragment : BaseFragment(){

	private var _binding: FragmentHomeBinding? = null

	private val binding
		get() = _binding!!

	private val viewModel by lazy { ViewModelProvider(this, InjectorProvider.getHomeViewModelFactory()).get(HomeViewModel::class.java) }

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

	private fun setupViews(){
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

		binding.viewHorizontalLine.setBackgroundColor(GlobalUtil.getThemeColor())
		initViewObservable()
	}

	//页面事件监听的方法，一般用于ViewModel层转到View层的事件注册
	private fun initViewObservable(){
		if (!viewModel.articleLiveData.hasObservers()) {
			viewModel.articleLiveData.observe(viewLifecycleOwner, Observer { result ->
				val response = result.getOrNull()
				if (response == null) {
					//ResponseHandler.getFailureTips(result.exceptionOrNull()).showToast()
					return@Observer
				}
				if (response.data.datas.isNullOrEmpty()) {
					return@Observer
				}
				viewModel.articleDataList.addAll(response.data.datas)
				/*val itemCount = replyAdapter.itemCount
				replyAdapter.notifyItemRangeInserted(itemCount, response.itemList.size)
				if (response.nextPageUrl.isNullOrEmpty()) {
					binding.refreshLayout.finishLoadMoreWithNoMoreData()
				} else {
					binding.refreshLayout.closeHeaderOrFooter()
				}*/
			})
		}
	}

	companion object {

		fun newInstance() = HomeFragment()
	}
}