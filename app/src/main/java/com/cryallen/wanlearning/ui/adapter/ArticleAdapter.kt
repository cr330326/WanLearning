package com.cryallen.wanlearning.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.extension.toHtml
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.utils.GlideLoadUtils
import com.cryallen.wanlearning.utils.LogUtils

/***
 * 文章适配器类
 * @author vsh9p8q
 * @DATE 2021/9/20
 ***/
class ArticleAdapter(data : MutableList<ModelResponse.Article>) :
	BaseQuickAdapter<ModelResponse.Article, BaseViewHolder>(R.layout.item_ariticle,data) {

	override fun convert(holder: BaseViewHolder, item: ModelResponse.Article) {
		holder.run {
			setGone(R.id.on_top,item.type != 1)
			setGone(R.id.fresh, !item.fresh)
			if (item.tags.isNotEmpty()) {
				setGone(R.id.project_type, false)
				setText(R.id.project_type, item.tags[0].name)
			} else {
				setGone(R.id.project_type, true)
			}

			setGone(R.id.thumbnail, item.envelopePic.isNullOrBlank())
			setGone(R.id.desc, item.desc.isNullOrBlank())

			setText(R.id.author,
				if (item.author.isNotEmpty()) item.author else item.shareUser
			)
			setText(R.id.date, item.niceDate)
			setText(R.id.title, item.title.toHtml())

			setText(
				R.id.chapter, when {
					item.superChapterName.isNotBlank() and item.chapterName.isNotBlank() ->
						"${item.superChapterName} / ${item.chapterName}"
					item.superChapterName.isNotBlank() -> item.superChapterName
					item.chapterName.isNotBlank() -> item.chapterName
					else -> ""
				}
			)
			setText(R.id.desc, item.desc)

			if (item.envelopePic.isNotBlank()) {
				GlideLoadUtils.loadImageFromUrl(getView(R.id.thumbnail),item.envelopePic)
			}
		}

		holder.itemView.setOnClickListener {
			/*Intent(context, WebActivity::class.java).run {
				putExtra(Constants.WEB_TITLE, item.title)
				putExtra(Constants.WEB_URL, item.link)
				context.startActivity(this)
			}*/
			LogUtils.d("item onclick start link:" + item.link)
		}
	}
}