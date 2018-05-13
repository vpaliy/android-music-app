package com.vpaliy.mediaplayer.domain.model

data class SearchPage(
    private var current: Int,
    var query: String? = null
) {

  val isFirst get() = current == 0

  fun invalidate() = apply { current = 0 }

  fun next() = apply { current++ }
}