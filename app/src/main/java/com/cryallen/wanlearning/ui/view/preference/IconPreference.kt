package  com.cryallen.wanlearning.ui.view.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.cryallen.wanlearning.R
import com.cryallen.wanlearning.utils.GlobalUtils


/***
 * 颜色选择器
 * @author vsh9p8q
 * @DATE 2021/9/23
 ***/
class IconPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

    var circleImageView: MyColorCircleView? = null

    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val color = GlobalUtils.getThemeColor()
        circleImageView = holder?.itemView?.findViewById(R.id.iv_preview)
        circleImageView?.color = color
        circleImageView?.border = color
    }

    fun setView() {
        val color = GlobalUtils.getThemeColor()
        circleImageView?.color = color
        circleImageView?.border = color
    }
}