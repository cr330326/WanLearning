package com.cryallen.wanlearning.exception

import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.model.enums.Error
import com.cryallen.wanlearning.utils.GlobalUtil

/***
 * 自定义错误信息异常
 * @author vsh9p8q
 * @DATE 2021/9/19
 ***/
class AppException : Exception{

	var errorMsg: String //错误消息
	var errCode: Int = 0 //错误码
	var errorLog: String? //错误日志
	var throwable: Throwable? = null

	constructor(errCode: Int, error: String?, errorLog: String? = "", throwable: Throwable? = null) : super(error) {
		this.errorMsg = error ?: GlobalUtil.getString(R.string.network_request_unknown_error)
		this.errCode = errCode
		this.errorLog = errorLog ?: this.errorMsg
		this.throwable = throwable
	}

	constructor(error: Error, e: Throwable?) {
		errCode = error.getKey()
		errorMsg = error.getValue()
		errorLog = e?.message
		throwable = e
	}
}