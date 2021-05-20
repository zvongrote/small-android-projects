package com.zachvg.news.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zachvg.news.R
import com.zachvg.news.ui.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val viewModel: NewsViewModel by activityViewModels()
}