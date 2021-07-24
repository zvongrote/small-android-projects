package com.zachvg.currencyconverter.models

import com.squareup.moshi.Json

data class AllCurrenciesDto(
    @Json(name = "results")
    val results: Map<String?, CurrencyInfoDto?>
)