package com.cryallen.wanlearning.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import androidx.lifecycle.Observer
import com.cryallen.wanlearning.appViewModel
import com.cryallen.wanlearning.base.BaseActivity
import com.cryallen.wanlearning.databinding.ActivityWebBinding
import com.cryallen.wanlearning.extension.preCreateSession
import com.cryallen.wanlearning.ui.ext.initBack
import com.cryallen.wanlearning.ui.ext.loadServiceInit
import com.cryallen.wanlearning.ui.ext.showLoading
import com.cryallen.wanlearning.ui.vassonic.OfflinePkgSessionConnection
import com.cryallen.wanlearning.ui.vassonic.SonicJavaScriptInterface
import com.cryallen.wanlearning.ui.vassonic.SonicRuntimeImpl
import com.cryallen.wanlearning.ui.vassonic.SonicSessionClientImpl
import com.cryallen.wanlearning.utils.GlobalUtils
import com.cryallen.wanlearning.utils.LogUtils
import com.kingja.loadsir.core.LoadService
import com.tencent.sonic.sdk.*

/***
 * webview Activity
 * @author vsh9p8q
 * @DATE 2021/9/24
 ***/
class WebViewActivity : BaseActivity() {

	private var _binding: ActivityWebBinding? = null

	private val binding: ActivityWebBinding get() = _binding!!

	private var title: String = ""

	private var linkUrl: String = ""

	private var isTitleFixed: Boolean = false

	private var sonicSession: SonicSession? = null

	private var sonicSessionClient: SonicSessionClientImpl? = null

	private var mode: Int = MODE_DEFAULT

	private lateinit var loadService: LoadService<Any>

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initParams()
		preloadInitVasSonic()
		_binding = ActivityWebBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	override fun onDestroy() {
		binding.webView.destroy()
		sonicSession?.destroy()
		sonicSession = null
		_binding = null
		super.onDestroy()
	}

	private fun initParams() {
		title = intent.getStringExtra(TITLE) ?: GlobalUtils.appName
		linkUrl = intent.getStringExtra(LINK_URL) ?: DEFAULT_URL
		isTitleFixed = intent.getBooleanExtra(IS_TITLE_FIXED, false)
		mode = intent.getIntExtra(PARAM_MODE, MODE_DEFAULT)
	}

	override fun setupViews(){
		initTitleBar()
		initWebView()
		if (sonicSessionClient != null) {
			sonicSessionClient?.bindWebView(binding.webView)
			sonicSessionClient?.clientReady()
		} else {
			binding.webView.loadUrl(linkUrl)
		}

		appViewModel.appColor.observe(this, Observer {
			supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
			setStatusBarBackground()
		})
	}

	private fun initTitleBar() {
		//????????? toolbar
		binding.webToolbar.toolbar.run {
			initBack {
				//??????????????????
				this@WebViewActivity.finish()
			}
		}
		binding.webToolbar.toolbar.title = title
		//????????????????????????
		loadService = loadServiceInit(binding.webView){
			//start(this, title, linkUrl,false)
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private fun initWebView() {
		binding.webView.settings.run {
			mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
			javaScriptEnabled = true
			binding.webView.removeJavascriptInterface("searchBoxJavaBridge_")
			intent.putExtra(SonicJavaScriptInterface.PARAM_LOAD_URL_TIME, System.currentTimeMillis())
			binding.webView.addJavascriptInterface(SonicJavaScriptInterface(sonicSessionClient, intent), "sonic")
			allowContentAccess = true
			databaseEnabled = true
			domStorageEnabled = true
			setAppCacheEnabled(true)
			savePassword = false
			saveFormData = false
			useWideViewPort = true
			loadWithOverviewMode = true
			defaultTextEncodingName = "UTF-8"
			setSupportZoom(true)
		}
		binding.webView.webChromeClient = UIWebChromeClient()
		binding.webView.webViewClient = UIWebViewClient()
		binding.webView.setDownloadListener { url, _, _, _, _ ->
			// ???????????????????????????
			val uri = Uri.parse(url)
			val intent = Intent(Intent.ACTION_VIEW, uri)
			startActivity(intent)
		}
	}

	/**
	 * ??????VasSonic????????????H5?????????????????????
	 */
	private fun preloadInitVasSonic() {
		window.addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED)

		// init sonic engine if necessary, or maybe u can do this when application created
		if (!SonicEngine.isGetInstanceAllowed()) {
			SonicEngine.createInstance(SonicRuntimeImpl(application), SonicConfig.Builder().build())
		}

		// if it's sonic mode , startup sonic session at first time
		if (MODE_DEFAULT != mode) { // sonic mode
			val sessionConfigBuilder = SonicSessionConfig.Builder()
			sessionConfigBuilder.setSupportLocalServer(true)

			// if it's offline pkg mode, we need to intercept the session connection
			if (MODE_SONIC_WITH_OFFLINE_CACHE == mode) {
				sessionConfigBuilder.setCacheInterceptor(object : SonicCacheInterceptor(null) {
					override fun getCacheData(session: SonicSession): String? {
						return null // offline pkg does not need cache
					}
				})
				sessionConfigBuilder.setConnectionInterceptor(object : SonicSessionConnectionInterceptor() {
					override fun getConnection(session: SonicSession, intent: Intent): SonicSessionConnection {
						return OfflinePkgSessionConnection(this@WebViewActivity, session, intent)
					}
				})
			}

			// create sonic session and run sonic flow
			sonicSession = SonicEngine.getInstance().createSession(linkUrl, sessionConfigBuilder.build())
			if (null != sonicSession) {
				sonicSession?.bindClient(SonicSessionClientImpl().also { sonicSessionClient = it })
			} else {
				// this only happen when a same sonic session is already running,
				// u can comment following codes to feedback as a default mode.
				// throw new UnknownError("create session fail!");
				LogUtils.d(TAG, "${title},${linkUrl}:create sonic session fail!")
			}
		}
	}

	inner class UIWebChromeClient : WebChromeClient() {
		override fun onReceivedTitle(view: WebView?, title: String?) {
			super.onReceivedTitle(view, title)
			LogUtils.d(TAG, "onReceivedTitle >>> title:${title}")
			if (!isTitleFixed) {
				title?.run {
					this@WebViewActivity.title = this
					binding.webToolbar.toolbar.title = this
				}
			}
		}
	}

	inner class UIWebViewClient : WebViewClient() {
		override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
			LogUtils.d(TAG, "onPageStarted >>> url:${url}")
			linkUrl = url
			super.onPageStarted(view, url, favicon)
			loadService.showLoading()
		}

		override fun onPageFinished(view: WebView, url: String) {
			LogUtils.d(TAG, "onPageFinished >>> url:${url}")
			super.onPageFinished(view, url)
			sonicSession?.sessionClient?.pageFinish(url)
			loadService.showSuccess()
		}

		override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
			if (sonicSession != null) {
				val requestResponse = sonicSessionClient?.requestResource(url)
				if (requestResponse is WebResourceResponse) return requestResponse
			}
			return null
		}
	}

	companion object {
		private const val TITLE = "title"

		private const val LINK_URL = "link_url"

		const val DEFAULT_URL = "https://github.com/cr330326/WanLearning"

		private const val IS_TITLE_FIXED = "isTitleFixed"

		const val PARAM_MODE = "param_mode"

		const val MODE_DEFAULT = 0

		const val MODE_SONIC = 1

		const val MODE_SONIC_WITH_OFFLINE_CACHE = 2

		val DEFAULT_TITLE = GlobalUtils.appName

		/**
		 * ??????WebView????????????
		 *
		 * @param context       ???????????????
		 * @param title         ??????
		 * @param url           ????????????
		 * @param isTitleFixed  ?????????????????????????????????????????????????????????????????????????????????true????????????false ?????????
		 * @param mode          ???????????????MODE_DEFAULT ????????????WebView?????????MODE_SONIC ??????VasSonic??????????????? MODE_SONIC_WITH_OFFLINE_CACHE ??????VasSonic??????????????????
		 */
		fun start(context: Context, title: String, url: String, isTitleFixed: Boolean = true, mode: Int = MODE_SONIC) {
			url.preCreateSession()  //?????????url
			val intent = Intent(context, WebViewActivity::class.java).apply {
				putExtra(TITLE, title)
				putExtra(LINK_URL, url)
				putExtra(IS_TITLE_FIXED, isTitleFixed)
				putExtra(PARAM_MODE, mode)
				putExtra(SonicJavaScriptInterface.PARAM_CLICK_TIME, System.currentTimeMillis())
			}
			context.startActivity(intent)
		}

	}
}