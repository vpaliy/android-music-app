package com.vpaliy.mediaplayer.domain.model

sealed class TrackType {
  object Favorite : TrackType()
  object History : TrackType()
}