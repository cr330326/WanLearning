package com.cryallen.wanlearning.data.remote

import com.cryallen.wanlearning.constant.CommonConfig
import com.cryallen.wanlearning.data.remote.callback.GsonTypeAdapterFactory
import com.cryallen.wanlearning.data.remote.interceptor.RxHttpLoggerInterceptor
import com.cryallen.wanlearning.data.remote.monitor.listener.NetworkListener
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
			//配置工厂监听器。主要是计算网络过程消耗时间
			.eventListenerFactory(NetworkListener.get())
			.addInterceptor(HeaderInterceptor())
			.addInterceptor(loggingInterceptor)

		return builder.build()
	}

	class HeaderInterceptor : Interceptor {
		override fun intercept(chain: Interceptor.Chain): Response {
			val original = chain.request()
			val request = original.newBuilder().apply {
				header("Content-type","application/json; charset=utf-8")
				header("User-Agent", System.getProperty("http.agent") ?: "unknown")
			}.build()
			return chain.proceed(request)
		}
	}
}