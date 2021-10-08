package com.cryallen.wanlearning.utils

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.WanApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/**
 * 应用程序全局的通用工具类，功能比较单一，经常被复用的功能，应该封装到此工具类当中，从而给全局代码提供方面的操作。
 * @author vsh9p8q
 * @DATE 2021/9/14
 */
object GlobalUtils {

    private var TAG = "GlobalUtil"

    /**
     * 获取随机一张图片路径
     */
    fun randomImage(): String {
        Random().run {
            val red = nextInt(IMAGE_URL.size)
            return IMAGE_URL[red]
        }
    }

    //网络图片数据 给用户随机使用
    var IMAGE_URL = arrayListOf(
        "https://img2.woyaogexing.com/2021/09/30/b6d9c96b85644f36a95de8d3763d2439!400x400.jpg",
        "https://img2.woyaogexing.com/2021/10/03/8c4abc72d4754bd591faf3ad521be4cc!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/03/b1d1a649bd1541e38249d16cee37b38a!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/03/958c86a2b7594f90bddfb54352ca837e!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/03/6c22c1cca8d04220bc73db7bb9c6a1df!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/a15389936a56420ab87bcceb92966170!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/0ff25015021149db9c5da770b5190080!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/7776787954b44d98ad8c6abc86a256e8!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/172200df78ca438f86aaa09219a8b6c2!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/1cf24f4faac646f6bc7f5c804f0e5c1b!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/47fb4abf8de24019a5abcf3db6a6022b!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/c4d09969143f40e189437f16025ac08a!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/10/02/eb894398b12f4863a701edcb8591b09d!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/28781aaa99694a779d8bcd3c11c74e8c!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/727fa089624944d2a1c192129d03ff97!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/d2f0927034ae456988c85f8438ea09a5!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/f89875e2283143afb658f17dd0c46bc3!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/9e72eba01da94ff6be3be7e1f306338f!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/072091861bbf4d3e93483fb1184fa519!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/22/4ea48b8a014d47de8962d14e536afc5a!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/1132d585b2804923a507f5b525ed8e26!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/27de38ac72f2406d9ed19b4e618ff72f!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/3cc8043053b643f5aefecb7b0d8e43f7!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/a12743e708994f23b43e2d59e54e52c9!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/be6f8560293d4d87b3816d8a058878c8!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/08/9137bb9442494215bbe5dd6ab54a0209!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/04/448871a112bf4b55a06fb81c839359a5!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/04/0b793e762bc949489ff4f619d8f958c5!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/04/c3238130815c4df19a04d04ad5202a3b!400x400.jpeg",
        "https://img2.woyaogexing.com/2021/09/04/38121c7a69ce44238a9928db9053936d!400x400.jpeg")

    /**
     * 获取当前应用程序的包名。
     *
     * @return 当前应用程序的包名。
     */
    val appPackage: String
        get() = WanApplication.instance.packageName

    /**
     * 获取当前应用程序的名称。
     * @return 当前应用程序的名称。
     */
    val appName: String
        get() = WanApplication.instance.resources.getString(WanApplication.instance.applicationInfo.labelRes)

    /**
     * 获取当前应用程序的版本名。
     * @return 当前应用程序的版本名。
     */
    val appVersionName: String
        get() = WanApplication.instance.packageManager.getPackageInfo(appPackage, 0).versionName

    /**
     * 获取当前应用程序的版本号。
     * @return 当前应用程序的版本号。
     */
    val appVersionCode: Long
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WanApplication.instance.packageManager.getPackageInfo(appPackage, 0).longVersionCode
        } else {
            WanApplication.instance.packageManager.getPackageInfo(appPackage, 0).versionCode.toLong()
        }

    /**
     * 获取设备的的型号，如果无法获取到，则返回Unknown。
     * @return 设备型号。
     */
    val deviceModel: String
        get() {
            var deviceModel = Build.MODEL
            if (TextUtils.isEmpty(deviceModel)) {
                deviceModel = "unknown"
            }
            return deviceModel
        }

    /**
     * 获取设备的品牌，如果无法获取到，则返回Unknown。
     * @return 设备品牌，全部转换为小写格式。
     */
    val deviceBrand: String
        get() {
            var deviceBrand = Build.BRAND
            if (TextUtils.isEmpty(deviceBrand)) {
                deviceBrand = "unknown"
            }
            return deviceBrand.toLowerCase(Locale.getDefault())
        }

    private var deviceSerial: String? = null

    /**
     * 获取设备的序列号。如果无法获取到设备的序列号，则会生成一个随机的UUID来作为设备的序列号，UUID生成之后会存入缓存，
     * 下次获取设备序列号的时候会优先从缓存中读取。
     * @return 设备的序列号。
     */
    @SuppressLint("HardwareIds")
    fun getDeviceSerial(): String {
        if (deviceSerial == null) {
            var deviceId: String? = null
            val appChannel = getApplicationMetaData("APP_CHANNEL")
            if ("google" != appChannel || "samsung" != appChannel) {
                try {
                    deviceId = Settings.Secure.getString(WanApplication.instance.contentResolver, Settings.Secure.ANDROID_ID)
                } catch (e: Exception) {
                    LogUtils.w(TAG, "get android_id with error", e)
                }
                if (!TextUtils.isEmpty(deviceId) && deviceId!!.length < 255) {
                    deviceSerial = deviceId
                    return deviceSerial.toString()
                }
            }
            var uuid = XKeyValue.get("uuid", "")
            if (!TextUtils.isEmpty(uuid)) {
                deviceSerial = uuid
                return deviceSerial.toString()
            }
            uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase(Locale.getDefault())
            CoroutineScope(Dispatchers.IO).launch { XKeyValue.put("uuid", uuid) }
            deviceSerial = uuid
            return deviceSerial.toString()
        } else {
            return deviceSerial.toString()
        }
    }

    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getString(resId: Int): String {
        return WanApplication.instance.resources.getString(resId)
    }

    /**
     * 获取资源文件中定义的字符串。
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getDimension(resId: Int): Int {
        return WanApplication.instance.resources.getDimensionPixelOffset(resId)
    }

    /**
     * 获取资源文件中定义的颜色
     *
     * @param resId
     * 字符串资源id
     * @return 字符串资源id对应的字符串内容。
     */
    fun getColor(resId: Int): Int {
        return ContextCompat.getColor(WanApplication.instance, resId)
    }

    /**
     * 获取指定资源名的资源id。
     *
     * @param name
     * 资源名
     * @param type
     * 资源类型
     * @return 指定资源名的资源id。
     */
    fun getResourceId(name: String, type: String): Int {
        return WanApplication.instance.resources.getIdentifier(name, type, appPackage)
    }

    /**
     * 获取AndroidManifest.xml文件中，<application>标签下的meta-data值。
     *
     * @param key
     *  <application>标签下的meta-data健
     */
    fun getApplicationMetaData(key: String): String? {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = WanApplication.instance.packageManager.getApplicationInfo(appPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            LogUtils.e(TAG, e.message, e)
        }
        if (applicationInfo == null) return ""
        return applicationInfo.metaData.getString(key)
    }

    /**
     * 判断某个应用是否安装。
     * @param packageName
     * 要检查是否安装的应用包名
     * @return 安装返回true，否则返回false。
     */
    fun isInstalled(packageName: String): Boolean {
        val packageInfo: PackageInfo? = try {
            WanApplication.instance.packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        return packageInfo != null
    }

    /**
     * 获取当前应用程序的图标。
     */
    fun getAppIcon(): Drawable {
        val packageManager = WanApplication.instance.packageManager
        val applicationInfo = packageManager.getApplicationInfo(appPackage, 0)
        return packageManager.getApplicationIcon(applicationInfo)
    }

    /**
     * 判断手机是否安装了QQ。
     */
    fun isQQInstalled() = isInstalled("com.tencent.mobileqq")

    /**
     * 判断手机是否安装了微信。
     */
    fun isWechatInstalled() = isInstalled("com.tencent.mm")

    /**
     * 判断手机是否安装了微博。
     * */
    fun isWeiboInstalled() = isInstalled("com.sina.weibo")


    /**
     * 获取当前主题颜色
     */
    fun getThemeColor(): Int {
        val defaultColor = getColor(R.color.colorPrimary)
        val color = XKeyValue.get("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }
    }

    /**
     * 设置主题颜色
     */
    fun setThemeColor(color: Int) {
        XKeyValue.put("color", color)
    }

    /**
     * 设置shap文件的颜色
     *
     * @param view
     * @param color
     */
    fun setShapColor(view: View, color: Int) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(color)
    }

    /**
     * 设置shap的渐变颜色
     */
    fun setShapColor(view: View, color: IntArray, orientation: GradientDrawable.Orientation) {
        val drawable = view.background as GradientDrawable
        drawable.orientation = orientation//渐变方向
        drawable.colors = color//渐变颜色数组
    }
}