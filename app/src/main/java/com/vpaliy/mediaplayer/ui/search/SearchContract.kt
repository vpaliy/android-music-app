package com.vpaliy.mediaplayer.ui.search

import android.support.annotation.StringRes

interface SearchContract {
  interface View<in T> {
    fun showLoading()
    fun hideLoading()
    fun showResult(list: List<T>)
    fun appendResult(list: List<T>)
    fun showMessage(@StringRes id: Int)
    fun error()
    fun empty()
  }

  interface Presenter<out T> {
    fun query(query: String?)
    fun more()
    fun refresh()
    fun attachView(view: View<T>)
  }
}