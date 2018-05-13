package com.vpaliy.mediaplayer.ui.base

import android.support.v7.app.AppCompatActivity
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
  protected val navigator: Navigator by inject()
}