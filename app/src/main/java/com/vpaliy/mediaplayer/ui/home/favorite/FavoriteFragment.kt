package com.vpaliy.mediaplayer.ui.home.favorite

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.Params
import com.vpaliy.mediaplayer.injectWith
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import org.koin.android.ext.android.inject

class FavoriteFragment : HomeFragment() {
  override val presenter: Presenter by injectWith(Params.FAVORITE)
  override fun emptyMessage(): Int = R.string.empty_liked
  override fun alertMessage(): String = getString(R.string.loved_alert)
}
