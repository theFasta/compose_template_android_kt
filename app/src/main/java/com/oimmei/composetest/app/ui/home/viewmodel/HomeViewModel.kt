package com.oimmei.composetest.app.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 29/09/2023 - 13:17
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

class HomeViewModel(val aValue: Boolean) : ViewModel() {
    class HomeViewModelFactory(private val aValue: Boolean) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(aValue) as T
    }
}