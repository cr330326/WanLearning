package com.cryallen.wanlearning.data.persistence

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cryallen.wanlearning.WanApplication
import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.data.persistence.dao.ArticleDao
import com.cryallen.wanlearning.data.persistence.entity.ArticleEntity
import com.cryallen.wanlearning.utils.LogUtils

/***
 * 应用本地数据库
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

	abstract fun articleDao(): ArticleDao

	companion object {
		val TAG: String = this.javaClass.simpleName

		//单例模式 线程安全
		@Volatile private var instance: LocalDatabase? = null

		private fun getInstance(): LocalDatabase {
			return instance ?: synchronized(this) {
				instance ?: buildDatabase().also { instance = it }
			}
		}

		// Create and pre-populate the database. See this article for more details:
		// https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
		private fun buildDatabase(): LocalDatabase {
			return Room.databaseBuilder(WanApplication.instance, LocalDatabase::class.java, CommonConfig.DATABASE_NAME)
				.addCallback(
					object : RoomDatabase.Callback() {
						override fun onCreate(db: SupportSQLiteDatabase) {
							super.onCreate(db)
							LogUtils.d(TAG,"WanDatabase onCreate")
						}
					}
				)
				.build()
		}

		fun articleDao(): ArticleDao {
			return getInstance().articleDao()
		}
	}
}