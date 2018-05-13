package com.vpaliy.mediaplayer.data

import com.vpaliy.mediaplayer.CLIENT_ID
import com.vpaliy.soundcloud.model.TrackEntity
import java.util.LinkedList

open class Filter {
  fun filter(tracks: List<TrackEntity>?): List<TrackEntity>? {
    return tracks?.let {
      val list = LinkedList<TrackEntity>()
      tracks.forEach {
        val result = filter(it)
        result?.let { list.add(it) }
      }
      return if (list.isEmpty()) null else list
    }
  }

  private fun filter(trackEntity: TrackEntity?): TrackEntity? {
    return trackEntity?.let {
      if (trackEntity.artwork_url != null && trackEntity.is_streamable) {
        trackEntity.artwork_url = trackEntity.artwork_url.replace("medium", "t500x500")
        trackEntity.stream_url += "?client_id=$CLIENT_ID"
        return trackEntity
      }
      return null
    }
  }
}