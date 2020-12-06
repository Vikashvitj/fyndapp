package com.demoapp.encoraitunes.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.demoapp.encoraitunes.R


/**
 * Data Binding adapters specific to the app.
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }


    @JvmStatic
    @BindingAdapter("imageUrl")
    fun imageUrl(view: ImageView, url: String?) {
        if (url.isNullOrEmpty().not())
            Glide.with(view).load(url).diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(android.R.drawable.ic_media_play).into(view)
    }

    @JvmStatic
    @BindingAdapter("imageUrlThumbnail")
    fun imageUrlThumbnail(view: ImageView, url: String?) {
        if (url.isNullOrEmpty().not())
            Glide.with(view).load(url).placeholder(R.drawable.placeholder).diskCacheStrategy(DiskCacheStrategy.ALL).into(view)
    }
}
