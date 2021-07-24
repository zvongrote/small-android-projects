package com.zachvg.currencyconverter.utils

import com.zachvg.currencyconverter.BuildConfig

/*
This application depends on:
https://www.currencyconverterapi.com/

Get and API key and use the Secrets Gradle Plugin (https://github.com/google/secrets-gradle-plugin)
to expose it and keep it out of version control. Add the key to the "local.properties" file called
"apiKey" and everything should work.
 */
const val API_KEY = BuildConfig.apiKey

const val BASE_URL = "https://free.currconv.com"