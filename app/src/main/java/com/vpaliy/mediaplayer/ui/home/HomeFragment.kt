package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.*
import com.airbnb.lottie.LottieAnimationView
import com.vpaliy.kotlin_extensions.show
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.R.layout.fragment_home
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.startRefreshing
import com.vpaliy.mediaplayer.stopRefreshing
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

abstract class HomeFragment : BaseFragment(), HomeContract.View {
  private val progressHandler by lazy { Handler() }
  private val adapter: BaseAdapter<Track> by lazy {
    TrackAdapter(context!!, { navigator.navigate(activity!!, it) },
        { navigator.actions(activity!!, it) })
  }

  override val layout: Int
    get() = fragment_home

  override val status: LottieAnimationView
    get() = statusIndicator

  companion object {
    const val PROGRESS_DELAY = 300L
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
    view.let {
      refresher.setOnRefreshListener({ presenter.start() })
      list.adapter = adapter
      list.isNestedScrollingEnabled = false
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    super.onCreateOptionsMenu(menu, inflater)
    inflater?.inflate(R.menu.home, menu)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    if (item?.itemId == R.id.delete) {
      AlertDialog.Builder(context!!)
          .setTitle(R.string.erase_label)
          .setMessage(alertMessage())
          .setPositiveButton(getString(R.string.yes_label), { _, _ -> presenter.clearAll() })
          .setNegativeButton(getString(R.string.no_label), { dialog, _ -> dialog.dismiss() })
          .show()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onResume() {
    super.onResume()
    presenter.start()
  }

  override fun error() {
    setMenuVisibility(false)
  }

  override fun showEmpty() {
    adapter.clear()
    list.isNestedScrollingEnabled = false
    setMenuVisibility(false)
    statusIndicator.show()
  }

  override fun showLoading() {
    progressHandler.postDelayed(refresher::startRefreshing, PROGRESS_DELAY)
  }

  override fun hideLoading() {
    refresher.stopRefreshing()
    progressHandler.removeCallbacksAndMessages(null)
  }

  abstract fun alertMessage(): String

  override fun showTracks(list: List<Track>) {
    statusIndicator.visibility = View.GONE
    adapter.data = list.toMutableList()
    this.list.isNestedScrollingEnabled = list.size > 1
  }

  override fun removed(track: Track) = showMessage(R.string.removed_message)

  override fun showCleared() {
    showEmpty()
    showMessage(R.string.cleared_message)
  }

}
