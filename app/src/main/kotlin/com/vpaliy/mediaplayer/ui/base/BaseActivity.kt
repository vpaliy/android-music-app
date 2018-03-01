package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

  @Inject
  lateinit var navigator: Navigator

  abstract fun inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    inject()
    super.onCreate(savedInstanceState)
  }
}