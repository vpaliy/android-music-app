package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.Params
import com.vpaliy.mediaplayer.injectWith
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import org.koin.android.ext.android.inject

class HistoryFragment : HomeFragment() {
  override val presenter: Presenter by injectWith (Params.HISTORY)

  override fun emptyMessage(): Int = R.string.empty_history
  override fun alertMessage(): String = getString(R.string.history_alert)
}
