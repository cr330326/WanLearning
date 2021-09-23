package com.cryallen.wanlearning.utils

import android.graphics.Color
import java.util.*

/***
 * 颜色工具类
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
object ColorUtils {

	//自定义颜色，过滤掉与白色相近的颜色
	var ACCENT_COLORS = intArrayOf(
		Color.parseColor("#EF5350"),
		Color.parseColor("#EC407A"),
		Color.parseColor("#AB47BC"),
		Color.parseColor("#7E57C2"),
		Color.parseColor("#5C6BC0"),
		Color.parseColor("#42A5F5"),
		Color.parseColor("#29B6F6"),
		Color.parseColor("#26C6DA"),
		Color.parseColor("#26A69A"),
		Color.parseColor("#66BB6A"),
		Color.parseColor("#9CCC65"),
		Color.parseColor("#D4E157"),
		Color.parseColor("#FFEE58"),
		Color.parseColor("#FFCA28"),
		Color.parseColor("#FFA726"),
		Color.parseColor("#FF7043"),
		Color.parseColor("#8D6E63"),
		Color.parseColor("#BDBDBD"),
		Color.parseColor("#78909C"))

	/**
	 * 获取随机rgb颜色值
	 */
	fun randomColor(): Int {
		Random().run {
			//0-190, 如果颜色值过大,就越接近白色,就看不清了,所以需要限定范围
			val red = nextInt(190)
			val green = nextInt(190)
			val blue = nextInt(190)
			//使用rgb混合生成一种新的颜色,Color.rgb生成的是一个int数
			return Color.rgb(red, green, blue)
		}
	}
}