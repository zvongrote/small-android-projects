package com.zachvg.news.repository

import com.zachvg.news.api.RetrofitInstance
import com.zachvg.news.db.ArticleDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
}