package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import javax.inject.Inject
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_home.*

abstract class BaseFragment : Fragment() {

  @Inject
  protected lateinit var navigator: Navigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    retainInstance = true
    inject()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
      = inflater!!.inflate(layoutId(), container, false)!!

  @LayoutRes abstract fun layoutId(): Int

  protected open fun inject() {
  }

  protected fun showMessage(@StringRes res: Int)
      = Snackbar.make(rootView, res, 300).show()
}