package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.android.ext.android.inject

abstract class BaseFragment : Fragment() {

  protected val navigator: Navigator by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
      = inflater.inflate(layoutId(), container, false)!!

  @LayoutRes abstract fun layoutId(): Int

  protected fun showMessage(@StringRes res: Int)
      = Snackbar.make(rootView, res, 300).show()
}