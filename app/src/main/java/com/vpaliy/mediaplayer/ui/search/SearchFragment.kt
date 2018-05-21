package com.vpaliy.mediaplayer.ui.search

import android.os.Bundle
import android.os.Handler
import android.support.annotation.TransitionRes
import android.support.v4.app.Fragment
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.vpaliy.kotlin_extensions.hide
import com.vpaliy.kotlin_extensions.show
import com.vpaliy.kotlin_extensions.then
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.R.layout.fragment_search
import com.vpaliy.mediaplayer.startRefreshing
import com.vpaliy.mediaplayer.stopRefreshing
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.utils.OnReachBottomListener
import com.vpaliy.mediaplayer.ui.utils.showMessage
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject

abstract class SearchFragment<T> : BaseFragment(),
    SearchContract.View<T>, QueryCallback {

  protected abstract val adapter: BaseAdapter<T>
  private val handler by lazy { Handler() }

  override val status: LottieAnimationView
    get() = statusIndicator

  override val layout: Int
    get() = fragment_search

  private val onReachBottomListener: OnReachBottomListener by lazy(LazyThreadSafetyMode.NONE) {
    object : OnReachBottomListener(result.layoutManager) {
      override fun onLoadMore() {
        isLoading = true
        presenter.more()
        progress.postDelayed(progress::show, 500)
        handler.postDelayed({ progress?.hide(isGone = true) }, 2000)
      }
    }
  }

  override fun showResult(list: List<T>) {
    statusIndicator.hide(isGone = true)
    result.show()
    adapter.data = list.toMutableList()
  }

  override fun appendResult(list: List<T>) {
    statusIndicator.hide(isGone = true)
    adapter.appendData(list)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    refresher.setOnRefreshListener {
      presenter.refresh()
    }
    result.adapter = adapter
    result.addOnScrollListener(onReachBottomListener)
  }

  override fun inputCleared() {
    adapter.clear()
    refreshPage(false)
  }

  override fun queryTyped(query: String?) {
    presenter.query(query)
  }

  override fun showLoading() {
    handler.removeCallbacksAndMessages(null)
    handler.postDelayed(progress::show, 400)
  }

  override fun hideLoading() {
    handler.postDelayed({
      progress?.hide(isGone = true)
    }, 3000)
    onReachBottomListener.isLoading = false
  }

  override fun showRefreshing() {
    refresher.startRefreshing()
  }

  override fun hideRefreshing() {
    refresher.stopRefreshing()
  }

  override fun empty() {
    statusIndicator.show()
  }

  override fun error() {
    root.showMessage(R.string.error)
  }

  override fun onConnectionError() {
    handler.removeCallbacksAndMessages(null)
    progress.hide(isGone = true)
    super.onConnectionError()
  }

  private fun refreshPage(visible: Boolean) {
    val transition = getTransition(visible then R.transition.search_show ?: R.transition.search_show)
    TransitionManager.beginDelayedTransition(root, transition)
    result.visibility = visible then View.VISIBLE ?: View.GONE
  }

  private fun getTransition(@TransitionRes transitionId: Int): Transition {
    val inflater = TransitionInflater.from(context)
    return inflater.inflateTransition(transitionId)
  }
}