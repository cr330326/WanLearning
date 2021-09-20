package com.cryallen.wanlearning.model.bean

/**
 *  分页数据的基类
 */
data class ApiPagerResponse<T>(
    var curPage: Int,
    var datas: T,
    var offset: Int,
    var pageCount: Int,
    var size: Int,
    var total: Int,
    var over: Boolean
)