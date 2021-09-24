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

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;

import androidx.annotation.RestrictTo;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.TwoStatePreference;

import com.cryallen.wanlearning.R;
import com.cryallen.wanlearning.utils.GlobalUtils;

@SuppressLint("RestrictedApi")
public class CheckBoxPreference extends TwoStatePreference {
    private final Listener mListener = new Listener();
    CompoundButton checkBoxView;
    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckBoxPreference(
            Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes);

        setSummaryOn(TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOn,
                R.styleable.CheckBoxPreference_android_summaryOn));

        setSummaryOff(TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOff,
                R.styleable.CheckBoxPreference_android_summaryOff));

        setDisableDependentsState(TypedArrayUtils.getBoolean(a,
                R.styleable.CheckBoxPreference_disableDependentsState,
                R.styleable.CheckBoxPreference_android_disableDependentsState, false));

        a.recycle();
    }
    public CheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle,
                android.R.attr.checkBoxPreferenceStyle));
    }

    public CheckBoxPreference(Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        syncCheckboxView(holder.findViewById(android.R.id.checkbox));

        syncSummaryView(holder);
    }

    /**
     * @hide
     */
    @RestrictTo(LIBRARY)
    @Override
    protected void performClick(View view) {
        super.performClick(view);
        syncViewIfAccessibilityEnabled(view);
    }

    private void syncViewIfAccessibilityEnabled(View view) {
        AccessibilityManager accessibilityManager = (AccessibilityManager)
                getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            return;
        }

        View checkboxView = view.findViewById(android.R.id.checkbox);
        syncCheckboxView(checkboxView);

        View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    private void syncCheckboxView(View view) {
        if (view instanceof CompoundButton) {
            checkBoxView = ((CompoundButton) view);
            checkBoxView.setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(mChecked);
        }
        if (view instanceof CompoundButton) {
            checkBoxView.setOnCheckedChangeListener(mListener);
            checkBoxView.setButtonTintList(ColorStateList.valueOf(GlobalUtils.INSTANCE.getThemeColor()));
        }
    }

    public  void setBottonColor(){
        if(checkBoxView!=null){
            checkBoxView.setButtonTintList(ColorStateList.valueOf(GlobalUtils.INSTANCE.getThemeColor()));
        }
    }
    private class Listener implements CompoundButton.OnCheckedChangeListener {
        Listener() {}

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }
            CheckBoxPreference.this.setChecked(isChecked);
        }
    }
}
