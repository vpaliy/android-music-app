package com.vpaliy.mediaplayer.domain.model

sealed class RequestError
object Connection : RequestError()
object FailedRequest : RequestError()