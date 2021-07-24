package com.zachvg.currencyconverter.data.remote

import com.zachvg.currencyconverter.utils.API_KEY
import com.zachvg.currencyconverter.models.AllCurrenciesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyConverterService {

    @GET("/api/v7/currencies")
    suspend fun getAllCurrencies(@Query("apiKey") apiKey: String = API_KEY): Response<AllCurrenciesDto>

    // Gets the desired conversion rate and its inverse
    @GET("/api/v7/convert?compact=ultra")
    suspend fun convert(
        @Query("q") conversionString: String,
        @Query("apiKey") apiKey: String = API_KEY
    ) : Response<Map<String, Float>>
}