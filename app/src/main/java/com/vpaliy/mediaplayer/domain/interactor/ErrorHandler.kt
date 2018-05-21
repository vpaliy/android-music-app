package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.model.Connection
import com.vpaliy.mediaplayer.domain.model.RequestError
import com.vpaliy.mediaplayer.domain.model.FailedRequest
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ErrorHandler {
  fun handle(throwable: Throwable): RequestError {
    return when(throwable) {
      is UnknownHostException-> Connection
      is SocketTimeoutException-> Connection
      else -> FailedRequest
    }
  }
}