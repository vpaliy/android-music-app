package com.vpaliy.mediaplayer.domain.model

import android.text.format.DateUtils
import com.vpaliy.mediaplayer.ifNotEmpty

data class Track(var id: String? = null,
                 var streamUrl: String? = null,
                 var artworkUrl: String? = null,
                 var duration: String? = null,
                 var releaseDate: String? = null,
                 var title: String? = null,
                 var artist: String? = null,
                 var isLiked: Boolean = false,
                 var isSaved:Boolean=false){
    val formattedDuration: String?
        get()=duration ifNotEmpty DateUtils.formatElapsedTime(duration!!.toLong()/1000)
}