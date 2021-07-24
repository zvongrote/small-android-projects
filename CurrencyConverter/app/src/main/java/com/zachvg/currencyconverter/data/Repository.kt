package com.zachvg.currencyconverter.data

import com.zachvg.currencyconverter.data.remote.CurrencyConverterService
import com.zachvg.currencyconverter.models.CurrencyInfo
import com.zachvg.currencyconverter.utils.Resource
import com.zachvg.currencyconverter.utils.mapToListOfCurrencyInfo
import javax.inject.Inject

class Repository @Inject constructor(
    private val currencyConverterService: CurrencyConverterService
) {

    private var allCurrencyInfo: List<CurrencyInfo>? = null

    private val conversionRateCache = mutableMapOf<String, Float>()

    suspend fun getAllCurrencies(): Resource<List<CurrencyInfo>> {

        // Check if a cached version exists
        allCurrencyInfo?.let { return Resource.Success(it) }

        // If not, fetch it
        val allCurrenciesResponse = currencyConverterService.getAllCurrencies()

        return if (allCurrenciesResponse.isSuccessful) {
            allCurrencyInfo = allCurrenciesResponse.body()?.mapToListOfCurrencyInfo()
            Resource.Success(allCurrencyInfo ?: emptyList())
        } else {
            Resource.Error(allCurrenciesResponse.message() ?: "Unknown error message")
        }
    }

    // Converts the base amount to the target amount.
    // The cache is first checked for the desired conversion rate.
    // If it's not in the cache, then the rate is fetch. The inverse is also fetched at the same
    // time and added to the cache too since the user will probably want to convert back and forth.
    // Fetching both conversion rates at the same time helps reduce the amount of api calls.
    suspend fun convert(base: String, target: String, amount: Float): Resource<Float> {

        val conversionRate = conversionRateCache["${base}_$target"] ?: run {

            // Build the string for identifying the conversion desired and the inverse rate:
            // BASE_TARGET,TARGET_BASE
            val conversionString = "${base}_$target,${target}_$base"

            val conversionResponse = currencyConverterService.convert(conversionString)

            // Get the conversion rate or a return a Resource.Error
            if (conversionResponse.isSuccessful) {
                // Add the conversion rates to the cache
                conversionResponse.body()?.forEach { (conversionLabel, conversionRate) ->
                    conversionRateCache[conversionLabel] = conversionRate
                }
                conversionRateCache["${base}_$target"]!!
            } else {
                // Something went wrong, return an error
                return Resource.Error(conversionResponse.errorBody().toString())
            }
        }

        val convertedAmount = amount * conversionRate

        return Resource.Success(convertedAmount)
    }
}