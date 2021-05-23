package com.zachvg.news.repository

import com.zachvg.news.api.RetrofitInstance
import com.zachvg.news.db.ArticleDatabase
import com.zachvg.news.models.Article
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(val db: ArticleDatabase) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun detele(article: Article) = db.getArticleDao().deleteArticle(article)
}