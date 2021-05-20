package com.zachvg.news.repository

import com.zachvg.news.db.ArticleDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(val db: ArticleDatabase) {
}