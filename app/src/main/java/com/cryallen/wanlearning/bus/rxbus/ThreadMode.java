package com.cryallen.wanlearning.bus.rxbus;

/**
 *
 * Created by gorden on 2016/7/23.
 */
public enum ThreadMode {
    /**
     * current thread
     */
    CURRENT_THREAD,

    /**
     * android main thread
     */
    MAIN,

    /**
     * new thread
     */
    NEW_THREAD
}
