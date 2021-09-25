package com.cryallen.wanlearning.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.cryallen.wanlearning.model.bean.ModelResponse
import com.cryallen.wanlearning.ui.ext.showMessage
import java.io.File
import java.math.BigDecimal

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
    fun setIsLogin(isLogin: Boolean) {
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

    /**
     * 获取所有缓存数据
     */
    fun getTotalCacheSize(context: Context): String {

        var cacheSize = getFolderSize(context.cacheDir)
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            cacheSize += getFolderSize(context.externalCacheDir)
        }
        return getFormatSize(cacheSize.toDouble())
    }

    /**
     * 获取文件
     * Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
     * 目录，一般放一些长时间保存的数据
     * Context.getExternalCacheDir() -->
     * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
     */
    fun getFolderSize(file: File?): Long {
        var size: Long = 0
        file?.run {
            try {
                val fileList = listFiles()
                for (i in fileList.indices) {
                    // 如果下面还有文件
                    size += if (fileList[i].isDirectory) {
                        getFolderSize(fileList[i])
                    } else {
                        fileList[i].length()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return size
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {

        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return size.toString() + "Byte"
        }

        val megaByte = kiloByte / 1024

        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024

        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024

        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB"
        }

        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }

    /**
     * 清理所有缓存数据
     */
    fun clearAllCache(activity: AppCompatActivity?) {
        activity?.let {
            deleteDir(it.cacheDir)
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                if (it.externalCacheDir == null) {
                    it.showMessage("清理缓存失败")
                }
                return
            }
            it.externalCacheDir?.let { file ->
                if(deleteDir(file)){
                    activity.showMessage("清理缓存成功")
                }
            }
        }
    }

    private fun deleteDir(dir: File): Boolean {
        if (dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
        }
        return dir.delete()
    }
}