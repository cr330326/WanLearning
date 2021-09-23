
package com.cryallen.wanlearning.ui.view.textview

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.utils.TypeFaceUtils

/**
 * 带有自定义字体TextView。
 *
 * @author vsh9p8q
 * @DATE 2021/9/14
 */
class TypefaceTextView : AppCompatTextView {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.TypefaceTextView, 0, 0)
            val typefaceType = typedArray.getInt(R.styleable.TypefaceTextView_typeface, 0)
            typeface = getTypeface(typefaceType)
            typedArray.recycle()
        }
    }

    companion object {

        /**
         * 根据字体类型，获取自定义字体。
         */
        fun getTypeface(typefaceType: Int?) = when (typefaceType) {
            TypeFaceUtils.FZLL_TYPEFACE -> TypeFaceUtils.getFzlLTypeface()
            TypeFaceUtils.FZDB1_TYPEFACE -> TypeFaceUtils.getFzdb1Typeface()
            TypeFaceUtils.FUTURA_TYPEFACE -> TypeFaceUtils.getFuturaTypeface()
            TypeFaceUtils.DIN_TYPEFACE -> TypeFaceUtils.getDinTypeface()
            TypeFaceUtils.LOBSTER_TYPEFACE -> TypeFaceUtils.getLobsterTypeface()
            else -> Typeface.DEFAULT
        }
    }
}