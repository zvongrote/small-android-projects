package com.zachvg.currencyconverter.models

import com.squareup.moshi.Json

data class CurrencyInfoDto(
    @Json(name = "currencyName")
    val currencyName: String?,

    @Json(name = "currencySymbol")
    val currencySymbol: String?,

    @Json(name = "id")
    val id: String?
)