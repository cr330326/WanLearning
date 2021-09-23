package com.cryallen.wanlearning.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.extension.toHtml
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.utils.ColorUtils

/***
 * 知识体系子数据适配器类
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class KnowledgeChildAdapter(data: ArrayList<ModelResponse.Chapter>) :
    BaseQuickAdapter<ModelResponse.Chapter, BaseViewHolder>(R.layout.flow_layout, data) {

    override fun convert(holder: BaseViewHolder, item: ModelResponse.Chapter) {
        holder.setText(R.id.flow_tag,item.name.toHtml())
        holder.setTextColor(R.id.flow_tag, ColorUtils.randomColor())
    }

}