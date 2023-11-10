package com.oimmei.oipharma.app.utils


/**
 * Created by Andrea Fastame
 * @email andrea@oimmei.com
 * @since 10/11/2023 - 09:31
 * Copyright Oimmei Digital Consulting Srl 2015-2023 - www.oimmei.com
 */

class Constants {
    companion object {
        enum class ROUTES_PHARMA_LIST(val route: String) {
            pharmaList("pharmaList"),
            pharmaDetail("pharmaDetail/{json}")
        }
    }
}