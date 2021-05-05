package com.kiwi.task.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kiwi.task.R
import com.squareup.picasso.Picasso

class BindingUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun loadImage(imageView: ImageView, imageUrl: String?) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder).into(imageView)
        }
    }
}