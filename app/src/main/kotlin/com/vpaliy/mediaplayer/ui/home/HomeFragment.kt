package com.vpaliy.mediaplayer.ui.home

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.*
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import kotlinx.android.synthetic.main.fragment_home.*

abstract class HomeFragment : BaseFragment(), HomeContract.View {
  private lateinit var adapter: BaseAdapter<Track>
  abstract var presenter: Presenter?

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setHasOptionsMenu(true)
    view?.let {
      refresher.setOnRefreshListener({ presenter?.start() })
      adapter = TrackAdapter(context, { navigator.navigate(activity, it) }, { navigator.actions(activity, it) })
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
      AlertDialog.Builder(context)
          .setTitle(R.string.erase_label)
          .setMessage(alertMessage())
          .setPositiveButton(getString(R.string.yes_label), { _, _ -> presenter?.clear() })
          .setNegativeButton(getString(R.string.no_label), { dialog, _ -> dialog.dismiss() })
          .show()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onResume() {
    super.onResume()
    presenter?.start()
  }

  override fun error() {
    setMenuVisibility(false)
    empty.visibility = View.VISIBLE
  }

  override fun empty() {
    adapter.clear()
    list.isNestedScrollingEnabled = false
    setMenuVisibility(false)
    empty.setText(emptyMessage())
    empty.visibility = View.VISIBLE
  }

  override fun setLoading(isLoading: Boolean) {
    refresher.isRefreshing = isLoading
  }

  abstract fun alertMessage(): String

  override fun show(list: List<Track>) {
    empty.visibility = View.GONE
    adapter.data = list.toMutableList()
    this.list.isNestedScrollingEnabled = list.size > 1
  }

  override fun layoutId() = R.layout.fragment_home

  override fun removed(track: Track) = showMessage(R.string.removed_message)

  override fun cleared() {
    empty()
    showMessage(R.string.cleared_message)
  }

  abstract fun emptyMessage(): Int
}
