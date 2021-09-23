package com.cryallen.wanlearning.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.extension.toHtml
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.utils.ColorUtils

/***
 * 导航子数据适配器类
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class NavigationChildAdapter(data: ArrayList<ModelResponse.Article>) :
    BaseQuickAdapter<ModelResponse.Article, BaseViewHolder>(R.layout.flow_layout, data) {

    override fun convert(holder: BaseViewHolder, item: ModelResponse.Article) {
        holder.setText(R.id.flow_tag,item.title.toHtml())
        holder.setTextColor(R.id.flow_tag, ColorUtils.randomColor())
    }

}