package com.vpaliy.mediaplayer.ui.settings

import com.airbnb.lottie.LottieAnimationView
import com.vpaliy.mediaplayer.R.layout.fragment_settings
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment: BaseFragment() {
  override val layout: Int
    get() = fragment_settings

  override val status: LottieAnimationView
    get() = settingsIcon
}