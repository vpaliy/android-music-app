package com.vpaliy.mediaplayer.ui.utils

import android.transition.Transition

open class TransitionAdapterListener : Transition.TransitionListener {
  override fun onTransitionCancel(transition: Transition) {}
  override fun onTransitionEnd(dummy: Transition) {}
  override fun onTransitionPause(transition: Transition) {}
  override fun onTransitionResume(transition: Transition) {}
  override fun onTransitionStart(transition: Transition) {}
}
