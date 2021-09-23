package com.cryallen.wanlearning.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.extension.toHtml
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent

/***
 * 导航数据适配器类
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class NaviAdapter(data : MutableList<ModelResponse.Navigation>):
	BaseQuickAdapter<ModelResponse.Navigation, BaseViewHolder>(R.layout.item_system,data)  {

	override fun convert(holder: BaseViewHolder, item: ModelResponse.Navigation) {
		holder.run {
			setText(R.id.item_system_title, item.name.toHtml())

			getView<RecyclerView>(R.id.item_system_rv).run {
				val foxLayoutManager: FlexboxLayoutManager by lazy {
					FlexboxLayoutManager(context).apply {
						//方向 主轴为水平方向，起点在左端
						flexDirection = FlexDirection.ROW
						//左对齐
						justifyContent = JustifyContent.FLEX_START
					}
				}
				layoutManager = foxLayoutManager
				setHasFixedSize(true)
				isNestedScrollingEnabled = false
				adapter = NavigationChildAdapter(item.articles).apply {
					setOnItemClickListener { _, view, position ->
						//navigationAction.invoke(item.articles[position], view)
					}
				}
			}


		}


	}
}