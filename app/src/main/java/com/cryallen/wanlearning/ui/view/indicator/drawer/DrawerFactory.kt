package com.cryallen.wanlearning.ui.view.indicator.drawer

import com.cryallen.wanlearning.ui.view.indicator.enums.IndicatorStyle
import com.cryallen.wanlearning.ui.view.indicator.option.IndicatorOptions

/**
 * <pre>
 * Created by zhpan on 2019/11/24.
 * Description: Indicator Drawer Factory.
</pre> *
 */
internal object DrawerFactory {
  fun createDrawer(indicatorOptions: IndicatorOptions): IDrawer {
    return when (indicatorOptions.indicatorStyle) {
        IndicatorStyle.DASH -> DashDrawer(indicatorOptions)
        IndicatorStyle.ROUND_RECT -> RoundRectDrawer(indicatorOptions)
      else -> CircleDrawer(indicatorOptions)
    }
  }
}
