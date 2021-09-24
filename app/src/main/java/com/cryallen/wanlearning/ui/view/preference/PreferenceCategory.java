/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cryallen.wanlearning.ui.view.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat.CollectionItemInfoCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceViewHolder;

import com.cryallen.wanlearning.R;
import com.cryallen.wanlearning.utils.GlobalUtils;

/**
 * 自定义PreferenceCategory 拓展了一下，
 * 可以设置标题颜色
 * @author vsh9p8q
 * @DATE 2021/9/23
 */
public class PreferenceCategory extends PreferenceGroup {

    TextView titleView;

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @SuppressLint("RestrictedApi")
    public PreferenceCategory(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.preferenceCategoryStyle,
                android.R.attr.preferenceCategoryStyle));
    }

    public PreferenceCategory(Context context) {
        this(context, null);
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean shouldDisableDependents() {
        return !super.isEnabled();
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        if (Build.VERSION.SDK_INT >= VERSION_CODES.P) {
            holder.itemView.setAccessibilityHeading(true);
            titleView = (TextView) holder.findViewById(android.R.id.title);
            if (titleView == null) {
                return;
            }
            titleView.setTextColor(GlobalUtils.INSTANCE.getThemeColor());
        } else if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP) {
            // We can't safely look for colorAccent in the category layout XML below Lollipop, as it
            // only exists within AppCompat, and will crash if using a platform theme. We should
            // still try and parse the attribute here in case we are running inside
            // PreferenceFragmentCompat with an AppCompat theme, and to set the category title
            // accordingly.
            final TypedValue value = new TypedValue();
            if (!getContext().getTheme().resolveAttribute(R.attr.colorAccent, value, true)) {
                // Return if the attribute could not be resolved
                return;
            }
            titleView = (TextView) holder.findViewById(android.R.id.title);
            if (titleView == null) {
                return;
            }
            final int fallbackColor = ContextCompat.getColor(getContext(),
                    R.color.preference_fallback_accent_color);
            // If the current color is not the fallback color we hardcode in the layout XML,
            // then this has already been handled by developers and we shouldn't override the
            // color.
            if (titleView.getCurrentTextColor() != fallbackColor) {
                return;
            }
            titleView.setTextColor(GlobalUtils.INSTANCE.getThemeColor());
        }
    }

    public void setTitleColor(int color) {
        if (titleView != null) {
            titleView.setTextColor(color);
        }
    }

    /**
     * @deprecated Super class Preference deprecated this API.
     */
    @Deprecated
    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat info) {
        super.onInitializeAccessibilityNodeInfo(info);
        if (Build.VERSION.SDK_INT < VERSION_CODES.P) {
            CollectionItemInfoCompat existingItemInfo = info.getCollectionItemInfo();
            if (existingItemInfo == null) {
                return;
            }

            final CollectionItemInfoCompat newItemInfo = CollectionItemInfoCompat.obtain(
                    existingItemInfo.getRowIndex(),
                    existingItemInfo.getRowSpan(),
                    existingItemInfo.getColumnIndex(),
                    existingItemInfo.getColumnSpan(),
                    true,
                    existingItemInfo.isSelected());
            info.setCollectionItemInfo(newItemInfo);
        }
    }
}
