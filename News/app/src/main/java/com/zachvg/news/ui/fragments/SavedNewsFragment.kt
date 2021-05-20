package com.zachvg.news.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.zachvg.news.R
import com.zachvg.news.ui.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    private val viewModel: NewsViewModel by activityViewModels()
}