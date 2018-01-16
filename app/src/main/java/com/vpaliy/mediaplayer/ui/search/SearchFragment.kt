package com.vpaliy.mediaplayer.ui.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vpaliy.kotlin_extensions.hide
import com.vpaliy.kotlin_extensions.show
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.addReachBottomListener
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import kotlinx.android.synthetic.main.fragment_search.*

abstract class SearchFragment<T> : Fragment(), SearchContract.View<T>, QueryCallback {
  abstract val adapter: BaseAdapter<T>
  abstract var presenter: SearchContract.Presenter<T>?

  abstract fun inject()

  override fun show(list: List<T>) {
    empty.hide(isGone = true)
    adapter.data = list.toMutableList()
  }

  override fun append(list: List<T>) {
    empty.hide(isGone = true)
    adapter.appendData(list)
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater!!.inflate(R.layout.fragment_search, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    inject()
    result.adapter = adapter
    result.addReachBottomListener({
      presenter?.more()
    })
  }

  override fun inputCleared() {
    adapter.clear()
  }

  override fun queryTyped(query: String?) {
    presenter?.query(query)
  }

  override fun showLoading() {
    progress.show()
  }

  override fun hideLoading() {
    progress.hide(isGone = true)
  }

  override fun empty() {
    empty.show()
  }

  override fun error() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun showMessage(id: Int) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}