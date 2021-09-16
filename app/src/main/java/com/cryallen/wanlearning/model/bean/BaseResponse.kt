package com.cryallen.wanlearning.model.bean

/***
 * 服务器返回数据的基类，必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
abstract class BaseResponse<T>{

	//抽象方法，用户的基类继承该类时，需要重写该方法
	abstract fun isSucces(): Boolean

	abstract fun getResponseData(): T

	abstract fun getResponseCode(): Int

	abstract fun getResponseMsg(): String
}