package com.vpaliy.mediaplayer.domain.model

import android.text.TextUtils
import android.text.format.DateUtils

class Track {

    var id: String? = null
    var streamUrl: String? = null
    var artworkUrl: String? = null
    var duration: String? = null
    var releaseDate: String? = null
    var title: String? = null
    var artist: String? = null
    var isLiked: Boolean = false
    var isSaved:Boolean=false

    val formatedDuration: String?
        get() {
            if (!TextUtils.isEmpty(duration)) {
                val time = java.lang.Long.parseLong(duration)
                return DateUtils.formatElapsedTime(time / 1000)
            }
            return null
        }
}