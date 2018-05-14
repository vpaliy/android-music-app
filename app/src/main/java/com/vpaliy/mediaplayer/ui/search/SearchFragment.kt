package com.vpaliy.mediaplayer.ui.search

import android.os.Bundle
import android.support.annotation.TransitionRes
import android.support.v4.app.Fragment
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vpaliy.kotlin_extensions.hide
import com.vpaliy.kotlin_extensions.show
import com.vpaliy.kotlin_extensions.then
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.utils.OnReachBottomListener
import com.vpaliy.mediaplayer.ui.utils.showMessage
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject

abstract class SearchFragment<T> : Fragment(), SearchContract.View<T>, QueryCallback {
  protected abstract val adapter: BaseAdapter<T>
  protected val navigator: Navigator by inject()

  private val onReachBottomListener:OnReachBottomListener by lazy(LazyThreadSafetyMode.NONE){
    object:OnReachBottomListener(result.layoutManager){
      override fun onLoadMore() {
        isLoading = true
        presenter.more()
      }
    }
  }

  override fun showResult(list: List<T>) {
    empty.hide(isGone = true)
    result.show()
    adapter.data = list.toMutableList()
  }

  override fun appendResult(list: List<T>) {
    empty.hide(isGone = true)
    adapter.appendData(list)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_search, container, false)
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
    refreshPage(false )
  }

  override fun queryTyped(query: String?) {
    presenter.query(query)
  }

  override fun showLoading() {
    progress.show()
  }

  override fun hideLoading() {
    progress.hide(isGone = true)
    onReachBottomListener.isLoading = false
  }

  override fun showRefreshing() {
    refresher.isRefreshing = true
  }

  override fun hideRefreshing() {
    refresher.isRefreshing = false
  }

  override fun empty() {
    empty.show()
  }

  override fun error() {
    root.showMessage(R.string.error)
  }

  override fun showMessage(id: Int) {
    root.showMessage(id)
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