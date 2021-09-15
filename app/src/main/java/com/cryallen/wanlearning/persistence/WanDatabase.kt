package com.cryallen.wanlearning.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cryallen.wanlearning.WanApplication
import com.cryallen.wanlearning.persistence.dao.ArticleDao
import com.cryallen.wanlearning.persistence.entity.ArticleEntity

/***
 * Wan App 数据库
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
@Database(entities = [ArticleEntity::class], version = 1)
abstract class WanDatabase : RoomDatabase() {

	abstract fun articleDao(): ArticleDao

	companion object {
		private val db: WanDatabase by lazy {
			Room.databaseBuilder(
				WanApplication.instance,
				WanDatabase::class.java, "database-wan"
			).build()
		}

		fun articleDao(): ArticleDao {
			return db.articleDao()
		}
	}
}