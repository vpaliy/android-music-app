package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.vpaliy.kotlin_extensions.*
import com.vpaliy.mediaplayer.R
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment() {
  protected val navigator: Navigator by inject()
  protected abstract val status: LottieAnimationView
  protected abstract val layout: Int

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
      = inflater.inflate(layout, container, false)

  protected fun showMessage(@StringRes res: Int)
      = Snackbar.make(rootView, res, 300).show()

  open fun onConnectionError() {
    status.setHeight(getDimensionPixelOffset(
        R.dimen.connection_error_height))
    status.setWidth(getDimensionPixelOffset(
        R.dimen.connection_error_width))
    status.setAnimation(R.raw.cloud_disconnection)
    status.show()
  }

  fun hideStatus() {
    status.hide(isGone = true)
  }
}