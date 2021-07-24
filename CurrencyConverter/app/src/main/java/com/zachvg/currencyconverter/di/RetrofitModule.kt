package com.zachvg.currencyconverter.di

import com.zachvg.currencyconverter.utils.BASE_URL
import com.zachvg.currencyconverter.data.remote.CurrencyConverterService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object  RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofitInstance() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyConverterService(retrofit: Retrofit): CurrencyConverterService {
        return retrofit.create(CurrencyConverterService::class.java)
    }
}