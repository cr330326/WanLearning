package com.cryallen.wanlearning.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.cryallen.wanlearning.base.BaseActivity
import com.cryallen.wanlearning.databinding.ActivityMainBinding

/***
 * 应用首页
 * @author vsh9p8q
 * @DATE 2021/9/14
 ***/
class MainActivity : BaseActivity() {
	private lateinit var viewBinding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewBinding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(viewBinding.root)
	}

	companion object {

		fun start(context: Context) {
			context.startActivity(Intent(context, MainActivity::class.java))
		}
	}
}