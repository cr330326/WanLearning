package com.cryallen.wanlearning.model.enums

import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.utils.GlobalUtil

/**
 * 错误枚举类
 * @author vsh9p8q
 * @DATE 2021/9/17
 */
enum class Error(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(1000, GlobalUtil.getString(R.string.network_request_unknown_error)),
    /**
     * 解析错误
     */
    PARSE_ERROR(1001, GlobalUtil.getString(R.string.network_request_parse_error)),
    /**
     * 网络错误
     */
    NETWORK_ERROR(1002, GlobalUtil.getString(R.string.network_request_connect_error)),

    /**
     * 证书出错
     */
    SSL_ERROR(1004, GlobalUtil.getString(R.string.network_request_ssl_error)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(1006, GlobalUtil.getString(R.string.network_request_timeout_error));

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}