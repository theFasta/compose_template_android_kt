package com.oimmei.oipharma.app.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.oimmei.oipharma.app.comms.model.Pharmacy


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 13/11/2023 - 11:20
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

object PharmaDetailViewModel : ViewModel() {
    var pharmacy: Pharmacy? = null
}