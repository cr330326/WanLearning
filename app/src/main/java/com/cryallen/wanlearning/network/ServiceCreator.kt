package com.cryallen.wanlearning.network

import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.network.callback.GsonTypeAdapterFactory
import com.cryallen.wanlearning.utils.JsonUtils
import com.cryallen.wanlearning.utils.LoggerUtils
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/***
 * 网络基础服务设置
 * @author vsh9p8q
 * @DATE 2021/9/15
 ***/
object ServiceCreator {

	private val builder = Retrofit.Builder()
		.baseUrl(CommonConfig.BASE_URL)
		.client(provideOKHttpClient())
		.addConverterFactory(ScalarsConverterFactory.create())
		.addConverterFactory(GsonConverterFactory.create(GsonBuilder().registerTypeAdapterFactory(GsonTypeAdapterFactory()).create()))

	private val retrofit = builder.build()

	fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

	private fun provideOKHttpClient(): OkHttpClient {
		val loggingInterceptor = HttpLoggingInterceptor(RxHttpLoggerInterceptor())
		loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

		val builder = OkHttpClient.Builder()
			//设置连接超时时间
			.connectTimeout(CommonConfig.NETWORK_CONNECT_TIME_OUT.toLong(),TimeUnit.SECONDS)
			.readTimeout(CommonConfig.NETWORK_CONNECT_TIME_OUT.toLong(),TimeUnit.SECONDS)
			.writeTimeout(CommonConfig.NETWORK_CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
			.retryOnConnectionFailure(true)
			.addInterceptor(HeaderInterceptor())
			.addInterceptor(loggingInterceptor)
		return builder.build()
	}

	class HeaderInterceptor : Interceptor {
		override fun intercept(chain: Interceptor.Chain): Response {
			val original = chain.request()
			val request = original.newBuilder().apply {
				header("Content-type","application/json; charset=utf-8")
				header("model", "Android")
				header("If-Modified-Since", "${Date()}")
				header("User-Agent", System.getProperty("http.agent") ?: "unknown")
			}.build()
			return chain.proceed(request)
		}
	}

	class RxHttpLoggerInterceptor : HttpLoggingInterceptor.Logger {
		private val mMessage = StringBuffer()
		private var jsonMessage = ""

		override fun log(message: String) {
			// 请求或者响应开始
			if (message.startsWith("--> POST")) {
				mMessage.setLength(0)
				mMessage.append(" ")
				mMessage.append("\r\n")
			}
			if (message.startsWith("--> GET")) {
				mMessage.setLength(0)
				mMessage.append(" ")
				mMessage.append("\r\n")
			}
			// 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
			// 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
			if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith("]")) {
				jsonMessage = JsonUtils.formatJson(message)
			}
			mMessage.append(jsonMessage)

			// 请求或者响应结束，打印整条日志
			if (message.startsWith("<-- END HTTP")) {
				LoggerUtils.d(mMessage.toString())
			}
		}
	}


}