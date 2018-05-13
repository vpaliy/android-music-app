package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment

class HistoryFragment : HomeFragment() {
  override var presenter: Presenter? = null
  override fun emptyMessage(): Int = R.string.empty_history
  override fun alertMessage(): String = getString(R.string.history_alert)
}
