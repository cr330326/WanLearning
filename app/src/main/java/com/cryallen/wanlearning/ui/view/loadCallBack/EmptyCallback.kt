package com.cryallen.wanlearning.ui.view.loadCallBack


import com.cryallen.wanlearning.R
import com.kingja.loadsir.callback.Callback


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}