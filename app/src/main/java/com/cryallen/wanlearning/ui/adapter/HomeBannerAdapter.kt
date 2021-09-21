package com.cryallen.wanlearning.ui.adapter

import android.widget.ImageView
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.extension.load
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.ui.view.bannerview.BaseBannerAdapter
import com.cryallen.wanlearning.ui.view.bannerview.BaseViewHolder
/***
 * 轮播图适配器类
 * @author vsh9p8q
 * @DATE 2021/9/21
 ***/
class HomeBannerAdapter : BaseBannerAdapter<ModelResponse.Banner>() {

    override fun bindData(holder: BaseViewHolder<ModelResponse.Banner>?, data: ModelResponse.Banner?, position: Int, pageSize: Int) {
        val img = holder!!.findViewById<ImageView>(R.id.item_banner_home_img)
        data?.let { img.load(it.imagePath,2f) }
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_banner_home
    }
}
