package com.vpaliy.mediaplayer.data

import com.vpaliy.soundcloud.model.TrackEntity
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class Filter @Inject constructor() {
  fun filter(tracks: List<TrackEntity>?): List<TrackEntity>? {
    return tracks?.let {
      val list = LinkedList<TrackEntity>()
      tracks.forEach {
        val result = filter(it)
        if (result != null) list.add(result)
      }
      return if (list.isEmpty()) null else list
    }
  }

  fun filter(trackEntity: TrackEntity?): TrackEntity? {
    return trackEntity?.let {
      if (trackEntity.artwork_url != null && trackEntity.is_streamable) {
        trackEntity.artwork_url = trackEntity.artwork_url.replace("large", "t500x500")
        trackEntity.stream_url += "?client_id="//+ Config.CLIENT_ID
        return trackEntity
      }
      return null
    }
  }
}