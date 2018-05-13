package com.vpaliy.mediaplayer.ui.home.favorite

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment

class FavoriteFragment : HomeFragment() {
  override var presenter: Presenter? = null

  override fun emptyMessage(): Int = R.string.empty_liked
  override fun alertMessage(): String = getString(R.string.loved_alert)
}
