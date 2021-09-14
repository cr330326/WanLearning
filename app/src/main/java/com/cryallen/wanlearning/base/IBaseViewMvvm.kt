package com.cryallen.wanlearning.base

/***
 * 基于MVVM模式下的 显示View接口
 * @author vsh9p8q
 * @DATE 2021/9/13
 ***/
interface IBaseViewMvvm : IBaseView {

	/**
	 * 初始化界面传递参数
	 */
	fun initParam()

	/**
	 * 初始化数据
	 */
	fun initData()

	/**
	 * 初始化界面观察者的监听
	 */
	fun initViewObservable()

	/**
	 * 显示内容
	 */
	fun showContent()

	/**
	 * 显示空页面
	 */
	fun showEmpty(message: String?)

	/**
	 * 显示失败
	 */
	fun showFailure(message: String?)

	/**
	 * 显示加载提示
	 */
	fun showLoading()
}