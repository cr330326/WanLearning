package com.cryallen.wanlearning.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import com.cryallen.wanlearning.base.BaseViewModel
import com.cryallen.wanlearning.repository.RemoteRepository
import com.cryallen.wanlearning.ui.databind.BooleanObservableField
import com.cryallen.wanlearning.ui.databind.StringObservableField

/***
 * 首页页面的ViewModel
 * @author vsh9p8q
 * @DATE 2021/9/25
 ***/
class LoginViewModel(private val repository: RemoteRepository) : BaseViewModel() {

	//用户名
	var username = StringObservableField()

	//密码(登录注册界面)
	var password = StringObservableField()

	//确认密码
	var confirmPassword = StringObservableField()

	//是否显示明文密码（登录注册界面）
	var isShowPwd = BooleanObservableField()

	var isConfirmShowPwd = BooleanObservableField()

	//用户名清除按钮是否显示, 不要在 xml 中写逻辑 所以逻辑判断放在这里
	var clearVisible = object : ObservableInt(username){
		override fun get(): Int {
			return if(username.get().isEmpty()){
				View.GONE
			}else{
				View.VISIBLE
			}
		}
	}

	//密码显示按钮是否显示, 不要在 xml 中写逻辑 所以逻辑判断放在这里
	var passwordVisible = object : ObservableInt(password){
		override fun get(): Int {
			return if(password.get().isEmpty()){
				View.GONE
			}else{
				View.VISIBLE
			}
		}
	}

	//确认密码显示按钮是否显示, 不要在 xml 中写逻辑 所以逻辑判断放在这里
	var confirmPasswordVisible = object : ObservableInt(confirmPassword){
		override fun get(): Int {
			return if(confirmPassword.get().isEmpty()){
				View.GONE
			}else{
				View.VISIBLE
			}
		}
	}


}