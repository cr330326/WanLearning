package com.cryallen.wanlearning.persistence.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cryallen.wanlearning.persistence.entity.ArticleEntity

/***
 * 文章数据访问类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
@Dao
interface ArticleDao {

	@Query("SELECT * FROM article")
	fun getAll(): List<ArticleEntity>

	@Query("SELECT * FROM article WHERE aid IN (:articleIds)")
	fun loadAllByIds(articleIds: IntArray): List<ArticleEntity>

	@Query("SELECT * FROM article WHERE title LIKE :title")
	fun loadByTitle(title: String): ArticleEntity

	@Query("SELECT * FROM article WHERE aid = :id")
	fun loadById(id: Int): ArticleEntity

	@Insert
	fun insertAll(vararg articles: ArticleEntity)

	@Delete
	fun delete(article: ArticleEntity)
}