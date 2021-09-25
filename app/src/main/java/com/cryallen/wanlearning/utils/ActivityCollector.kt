package com.cryallen.wanlearning.utils

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*
import kotlin.system.exitProcess

/***
 * 管理应用程序中所有Activity 单例
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
object ActivityCollector {

	private val activitys = Stack<WeakReference<Activity>>()

	/**
	 * 将Activity压入Application栈
	 *
	 * @param task 将要压入栈的Activity对象
	 */
	fun pushTask(task: WeakReference<Activity>?) {
		activitys.push(task)
	}

	/**
	 * 将传入的Activity对象从栈中移除
	 *
	 * @param task
	 */
	fun removeTask(task: WeakReference<Activity>?) {
		activitys.remove(task)
	}

	/**
	 * 根据指定位置从栈中移除Activity
	 *
	 * @param taskIndex Activity栈索引
	 */
	fun removeTask(taskIndex: Int) {
		if (activitys.size > taskIndex) activitys.removeAt(taskIndex)
	}

	/**
	 * 将栈中Activity移除至栈顶
	 */
	fun removeToTop() {
		val end = activitys.size
		val start = 1
		for (i in end - 1 downTo start) {
			val mActivity = activitys[i].get()
			if (null != mActivity && !mActivity.isFinishing) {
				mActivity.finish()
			}
		}
	}

	/**
	 * 移除全部（用于整个应用退出）
	 */
	fun removeAll() {
		for (task in activitys) {
			val mActivity = task.get()
			if (null != mActivity && !mActivity.isFinishing) {
				mActivity.finish()
			}
		}
	}

	/**
	 * 退出应用程序
	 */
	fun appExit() {
		try {
			removeAll()
			// 杀死该应用进程
			android.os.Process.killProcess(android.os.Process.myPid());
//            调用 System.exit(n) 实际上等效于调用：
//            Runtime.getRuntime().exit(0)
//            finish()是Activity的类方法，仅仅针对Activity，当调用finish()时，只是将活动推向后台，并没有立即释放内存，活动的资源并没有被清理；当调用System.exit(0)时，退出当前Activity并释放资源（内存），但是该方法不可以结束整个App如有多个Activty或者有其他组件service等不会结束。
//            其实android的机制决定了用户无法完全退出应用，当你的application最长时间没有被用过的时候，android自身会决定将application关闭了。
			exitProcess(0)
		} catch (e: Exception) {
			activitys.clear()
			e.printStackTrace()
		}
	}
}