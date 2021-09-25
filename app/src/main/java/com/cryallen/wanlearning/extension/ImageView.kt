/**
 * Kotlin扩展属性和扩展函数
 */
package com.cryallen.wanlearning.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * Glide加载图片，可以指定圆角弧度。
 *
 * @param url 图片地址
 * @param round 圆角，单位dp
 */
fun ImageView.load(url: String, round: Float = 0f) {
    if (round == 0f) {
        Glide.with(this.context).load(url).into(this)
    } else {
        val option = RequestOptions.bitmapTransform(RoundedCorners(dp2px(round)))
        Glide.with(this.context).load(url).apply(option).into(this)
    }
}

/**
 * Glide加载图片，可以定义配置参数。
 *
 * @param url 图片地址
 * @param options 配置参数
 */
fun ImageView.load(url: String, options: RequestOptions.() -> RequestOptions) {
    Glide.with(this.context).load(url).apply(RequestOptions().options()).into(this)
}

/**
 * Glide加载圆形图片
 *
 * @param url 图片地址
 */
fun ImageView.loadCircle(url: String) {
    Glide.with(this.context)
        .load(url)
        .apply(RequestOptions.bitmapTransform(CircleCrop()))
        .transition(DrawableTransitionOptions.withCrossFade(500))
        .into(this)
}