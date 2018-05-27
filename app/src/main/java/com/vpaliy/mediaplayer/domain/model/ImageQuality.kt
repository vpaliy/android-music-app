package com.vpaliy.mediaplayer.domain.model

sealed class ImageQuality
object Low : ImageQuality()
object Average : ImageQuality()
object High : ImageQuality()