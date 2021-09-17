package com.cryallen.wanlearning.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.data.remote.api.WanService
import com.cryallen.wanlearning.model.bean.ModelResponse

/***
 * Home页面分页数据源
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomePagingSource (private val wanService: WanService) : PagingSource<Int, ModelResponse.Article>() {

	override fun getRefreshKey(state: PagingState<Int, ModelResponse.Article>): Int? = null

	override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ModelResponse.Article> {
		val page = params.key ?: CommonConfig.PAGING_STARTING_PAGE_INDEX
		return try {
			val repoResponse = wanService.getArticles(page)
			val repoData = repoResponse.data
			val prevKey = if (page == CommonConfig.PAGING_STARTING_PAGE_INDEX) null else page - 1
			val nextKey = if (page == repoData.pageCount) null else page + 1
			LoadResult.Page(repoData.datas, prevKey, nextKey)
		} catch (e: Exception) {
			LoadResult.Error(e)
		}
	}
}