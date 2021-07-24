package com.zachvg.currencyconverter.utils

import com.zachvg.currencyconverter.models.AllCurrenciesDto
import com.zachvg.currencyconverter.models.CurrencyInfo
import com.zachvg.currencyconverter.models.CurrencyInfoDto


fun CurrencyInfoDto.mapToCurrencyInfo() = CurrencyInfo(
    id = id.orEmpty(),
    name = currencyName.orEmpty(),
    symbol = currencySymbol.orEmpty()
)

fun AllCurrenciesDto.mapToListOfCurrencyInfo(): List<CurrencyInfo> =
    results.values
        .filterNotNull()
        .map { it.mapToCurrencyInfo() }