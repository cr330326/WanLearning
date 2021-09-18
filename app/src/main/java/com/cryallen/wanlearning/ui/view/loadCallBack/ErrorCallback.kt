package com.cryallen.wanlearning.ui.view.loadCallBack

import android.content.Context
import android.view.View
import android.widget.Button
import com.cryallen.wanlearning.R
import com.kingja.loadsir.callback.Callback

/**
 * 自定义错误显示页面
 *
 * @author vsh9p8q
 * @DATE 2021/9/14
 */
class ErrorCallback : Callback() {

    private lateinit var mButton: Button

    var errorRetryListener: ErrorRetryListener? = null

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

    override fun onViewCreate(context: Context?, view: View?) {
        super.onViewCreate(context, view)
        mButton = view!!.findViewById(R.id.loading_error_btn)

        mButton.setOnClickListener {
            errorRetryListener!!.onRetry()
        }
    }
}

interface ErrorRetryListener {
    /**
     * 重试回调
     */
    fun onRetry()
}