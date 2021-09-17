package com.cryallen.wanlearning.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.repository.RemoteRepository
import kotlinx.coroutines.flow.Flow

/***
 * 首页页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	fun getArticlePagingData(): Flow<PagingData<ModelResponse.Article>> {
		return repository.getHomePagingData().cachedIn(viewModelScope)
	}
}