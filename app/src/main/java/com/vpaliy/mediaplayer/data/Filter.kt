package com.vpaliy.mediaplayer.data

import com.vpaliy.soundcloud.model.TrackEntity
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Filter @Inject
constructor() {

    fun filter(tracks: List<TrackEntity>?): List<TrackEntity>? {
        if (tracks != null) {
            val list = LinkedList<TrackEntity>()
            tracks.forEach {
                val result=filter(it)
                if (result != null) list.add(result)
            }
            return if (list.isEmpty()) null else list
        }
        return null
    }

    fun filter(trackEntity: TrackEntity?): TrackEntity? {
        if (trackEntity != null) {
            if (trackEntity.artwork_url != null && trackEntity.is_streamable) {
                trackEntity.artwork_url = trackEntity.artwork_url.replace("large", "t500x500")
                return trackEntity
            }
        }
        return null
    }
}