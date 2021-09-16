package com.cryallen.wanlearning.repository

import com.cryallen.wanlearning.data.persistence.LocalDatabase
import com.cryallen.wanlearning.data.persistence.dao.ArticleDao

/***
 * Repository层，数据源从本地数据库读取
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class LocalRepository {

	fun provideArticleDao(): ArticleDao {
		return LocalDatabase.articleDao()
	}

	companion object {
		val instance: LocalRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
			LocalRepository()
		}
	}
}