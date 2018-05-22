package com.vpaliy.mediaplayer.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.airbnb.lottie.LottieAnimationView
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.R.layout.fragment_settings
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.view.AlertFlashDialog
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {
  override val layout: Int
    get() = fragment_settings

  override val status: LottieAnimationView
    get() = settingsIcon


  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    clearAction.setOnClickListener { clearData() }
    inviteItem.setOnClickListener { invite() }
  }

  private fun invite() {
    val intent = Intent(Intent.ACTION_SEND)
        .putExtra(Intent.EXTRA_TEXT, getString(R.string.invite_text))
    intent.type = "text/plain"
    startActivity(Intent.createChooser(intent, getString(R.string.choose_to_share_text)))
  }

  private fun clearData() {
    context?.let {
      AlertFlashDialog.create(it, root)
          .setTitle(R.string.alert_clear_data_label)
          .show()
    }
  }
}