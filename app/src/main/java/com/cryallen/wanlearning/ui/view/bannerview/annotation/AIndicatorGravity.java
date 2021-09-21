package com.cryallen.wanlearning.ui.view.bannerview.annotation;

import static com.cryallen.wanlearning.ui.view.bannerview.constants.IndicatorGravity.CENTER;
import static com.cryallen.wanlearning.ui.view.bannerview.constants.IndicatorGravity.END;
import static com.cryallen.wanlearning.ui.view.bannerview.constants.IndicatorGravity.START;

import androidx.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <pre>
 *   Created by zhangpan on 2019-10-18.
 *   Description:指示器显示位置
 * </pre>
 */
@IntDef({ CENTER, START, END })
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PARAMETER)
public @interface AIndicatorGravity {

}
