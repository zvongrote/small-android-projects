package com.zachvg.news.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("image_url")
fun loadImageWithGlide(imageView: ImageView, urlToImage: String?) {
    urlToImage?.let {
        Glide.with(imageView).load(urlToImage).into(imageView)
    }
}
