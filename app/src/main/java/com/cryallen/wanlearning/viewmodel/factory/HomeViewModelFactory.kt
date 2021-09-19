package com.cryallen.wanlearning.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cryallen.wanlearning.repository.RemoteRepository
import com.cryallen.wanlearning.viewmodel.HomeViewModel

/***
 * 首页ViewModelFactory
 * @author vsh9p8q
 * @DATE 2021/9/17
 ***/
class HomeViewModelFactory(private val repository: RemoteRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}