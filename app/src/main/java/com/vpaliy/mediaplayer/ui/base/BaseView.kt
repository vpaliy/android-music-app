package com.vpaliy.mediaplayer.ui.base

interface BaseView<out T : BasePresenter> {
  val presenter: T
}