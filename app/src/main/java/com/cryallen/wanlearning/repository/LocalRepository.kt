package com.cryallen.wanlearning.repository

import com.cryallen.wanlearning.data.persistence.LocalDatabase
import com.cryallen.wanlearning.data.persistence.dao.ArticleDao

/***
 * Repository层，数据源从本地数据库读取
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
class LocalRepository private constructor(private val localDatabase: LocalDatabase) {

	fun provideArticleDao(): ArticleDao {
		return localDatabase.articleDao()
	}

	companion object {

		private var repository: LocalRepository? = null

		fun getInstance(local: LocalDatabase): LocalRepository {
			if (repository == null) {
				synchronized(LocalRepository::class.java) {
					if (repository == null) {
						repository = LocalRepository(local)
					}
				}
			}

			return repository!!
		}
	}
}