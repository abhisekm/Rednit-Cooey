package com.abhisek.rednit.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadLocalImage")
fun bindImageViewLoadLocalImage(imageView: ImageView, url: String) {
    val uri: Uri? = Uri.parse(url)
    uri?.let {
        Glide.with(imageView).load(it).into(imageView)
    }
}

@BindingAdapter("loadImageUpscale")
fun bindImageViewLoadImageUpscale(imageView: ImageView, url: String?) {
    url?.apply {
        val httpsUrl = replace("http:", "https:")
        val higerResUrl = httpsUrl.replace("200", "600").replace("300", "900")
        Glide.with(imageView).load(higerResUrl).into(imageView)
    }
}

@BindingAdapter("loadImage")
fun bindImageViewLoadImage(imageView: ImageView, url: String?) {
    url?.apply {
        val httpsUrl = replace("http:", "https:")
        Glide.with(imageView).load(httpsUrl).into(imageView)
    }
}