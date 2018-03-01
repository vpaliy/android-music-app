package com.vpaliy.mediaplayer.playback

import android.support.v4.media.MediaMetadataCompat
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope
import javax.inject.Inject

@PlaybackScope
class MetadataMapper @Inject constructor() : Mapper<MediaMetadataCompat, Track>() {
  override fun map(fake: Track?): MediaMetadataCompat? {
    return fake?.let {
      MediaMetadataCompat.Builder()
          .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, fake.artworkUrl)
          .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, fake.title)
          .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, fake.artist)
          .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, fake.duration!!.toLong())
          .putString(MediaMetadataCompat.METADATA_KEY_DATE, fake.releaseDate)
          .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, fake.streamUrl)
          .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, fake.id).build()
    }
  }

  override fun reverse(real: MediaMetadataCompat?): Track? {
    return real?.let {
      val track = Track()
      track.artist = real.getString(MediaMetadataCompat.METADATA_KEY_ARTIST)
      track.artworkUrl = real.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI)
      track.duration = real.getString(MediaMetadataCompat.METADATA_KEY_DURATION)
      track.releaseDate = real.getString(MediaMetadataCompat.METADATA_KEY_DATE)
      track.streamUrl = real.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
      track.id = real.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
      return track
    }
  }
}
