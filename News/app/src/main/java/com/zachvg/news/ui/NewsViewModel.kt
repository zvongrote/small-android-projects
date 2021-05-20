package com.zachvg.news.ui

import androidx.lifecycle.ViewModel
import com.zachvg.news.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(val newsRepository: NewsRepository) : ViewModel() {

}