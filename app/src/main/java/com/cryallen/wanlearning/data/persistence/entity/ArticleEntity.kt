package com.cryallen.wanlearning.data.persistence.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// 定义Article 数据库中的实体类，定义表名称，SQLite 中的表名称不区分大小写
@Entity(tableName = "article")
data class ArticleEntity(
    // 主键分配自动ID
    //@PrimaryKey(autoGenerate = true) val aid: Int,
    @PrimaryKey var aid: Int,
    // 如果您希望列具有不同的名称，请将 @ColumnInfo 注释添加到字段
    @ColumnInfo(name = "title") var title: String?,
    @ColumnInfo(name = "chapter_name") var chapterName: String?,
    @ColumnInfo(name = "author") var author: String?,
    @ColumnInfo(name = "nice_date") var niceDate: String?,
    @ColumnInfo(name = "link") var link: String?,
    @ColumnInfo(name = "super_chapter_id") var superChapterId: Int?,
    @ColumnInfo(name = "super_chapter_name") var superChapterName: String?
)