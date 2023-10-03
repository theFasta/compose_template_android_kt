package com.oimmei.composetest.app.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity

/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 22/01/2018 - 11:02
 * Copyright Oimmei Srls 2015,2016,2017 - www.oimmei.com
 */

class OIKeyboardUtils {
    companion object {
        fun hideKeyboard(activity: FragmentActivity) {
            try {
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
            }
        }

        fun hideKeyboard(activity: ComponentActivity) {
            try {
                (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
            }
        }
    }
}