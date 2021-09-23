package com.cryallen.wanlearning.utils

import android.text.TextUtils
import com.cryallen.wanlearning.model.bean.ModelResponse

/***
 * 缓存工具类
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
object CacheUtils {

    /**
     * 获取保存的账户信息
     */
    fun getUser(): ModelResponse.UserInfo? {
        val userStr = XKeyValue.get("user","")
        return if (TextUtils.isEmpty(userStr)) {
           null
        } else {
            JsonUtils.deserialize(userStr, ModelResponse.UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: ModelResponse.UserInfo?) {
        if (userResponse == null) {
            XKeyValue.put("user", "")
            setIsLogin(false)
        } else {
            XKeyValue.put("user", JsonUtils.serialize(userResponse))
            setIsLogin(true)
        }
    }

    /**
     * 是否已经登录
     */
    fun isLogin(): Boolean {
        return XKeyValue.get("login", false)
    }

    /**
     * 设置是否已经登录
     */
    private fun setIsLogin(isLogin: Boolean) {
        XKeyValue.put("login", isLogin)
    }

    /**
     * 首页是否开启获取指定文章
     */
    fun isNeedTop(): Boolean {
        return XKeyValue.get("top", false)
    }
    /**
     * 设置首页是否开启获取指定文章
     */
    fun setIsNeedTop(isNeedTop:Boolean) {
        return XKeyValue.put("top", isNeedTop)
    }
}