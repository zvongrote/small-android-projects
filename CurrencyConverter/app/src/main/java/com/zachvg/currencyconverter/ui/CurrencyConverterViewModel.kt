package com.zachvg.currencyconverter.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zachvg.currencyconverter.data.Repository
import com.zachvg.currencyconverter.models.CurrencyInfo
import com.zachvg.currencyconverter.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "curr-convt-vm-tag"

@HiltViewModel
class CurrencyConverterViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _isLoadingCurrencies = MutableLiveData(false)
    val isLoadingCurrencies: LiveData<Boolean> = _isLoadingCurrencies

    private val _noCurrenciesAvailable = MutableLiveData(false)
    val noCurrenciesAvailable: LiveData<Boolean> = _noCurrenciesAvailable

    private val _currenciesLoaded = MutableLiveData(false)
    val currenciesLoaded: LiveData<Boolean> = _currenciesLoaded

    private val _currencyDisplayStrings: MutableLiveData<List<String>> = MutableLiveData()
    val currencyDisplayStrings: LiveData<List<String>> = _currencyDisplayStrings

    private val _conversionResults: MutableLiveData<Pair<String, String>> = MutableLiveData()
    val conversionResults: LiveData<Pair<String, String>> = _conversionResults

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventFlow = eventChannel.receiveAsFlow()

    init {
        getAllCurrencies()
    }

    private fun getAllCurrencies() = viewModelScope.launch {

        _isLoadingCurrencies.value = true

        val allCurrenciesResource = repository.getAllCurrencies()

        when (allCurrenciesResource) {
            is Resource.Success -> {
                // Make sure currencies are available
                if (!allCurrenciesResource.data.isNullOrEmpty()) {
                    generateAndSetCurrencyStrings(allCurrenciesResource.data)
                    _noCurrenciesAvailable.value = false
                } else {
                    _noCurrenciesAvailable.value = true
                }
            }
            is Resource.Error -> {
                allCurrenciesResource.message?.let { Log.d(TAG, it) }
            }
            else -> {
                // Should never get here
                Log.e(TAG, "Something went wrong when loading all the currencies")
            }
        }

        // Add a delay so the loading animations can be seen briefly
        delay(2000)

        _isLoadingCurrencies.value = false
        _currenciesLoaded.value = true
    }

    private fun generateAndSetCurrencyStrings(currencyInfos: List<CurrencyInfo>) {
        val currencyInfoDisplayStrings = currencyInfos.map { currencyInfo ->
            currencyInfo.id + if (currencyInfo.symbol.isNotBlank()) " (${currencyInfo.symbol})" else ""
        }.sortedBy { it }

        _currencyDisplayStrings.postValue(currencyInfoDisplayStrings)
    }

    fun onConvertClick() = viewModelScope.launch { eventChannel.send(Event.Convert) }

    fun onSwapClick() = viewModelScope.launch { eventChannel.send(Event.SwapCurrencies) }

    fun convert(baseCode: String, targetCode: String, baseAmount: String?) = viewModelScope.launch {

        if (baseAmount.isNullOrEmpty() || baseAmount.isBlank()) {
            eventChannel.send(Event.InvalidAmount)
        } else {
            val convertedAmount = repository.convert(baseCode, targetCode, baseAmount.toFloat())

            when (convertedAmount) {
                is Resource.Success -> {
                    val conversionResults =
                        "$baseAmount $baseCode" to "${convertedAmount.data} $targetCode"

                    _conversionResults.postValue(conversionResults)
                }
                is Resource.Error -> {
                    // TODO
                }
                else -> {
                    Log.e(TAG, "Something went wrong")
                }
            }
        }
    }

    sealed class Event {
        object Convert : Event()
        object InvalidAmount : Event()
        object SwapCurrencies : Event()
    }
}