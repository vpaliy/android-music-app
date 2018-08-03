package com.vpaliy.mediaplayer.ui.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.vpaliy.mediaplayer.R

class AlertFlashDialog constructor(
        private val dialog: Dialog,
        private val yes: () -> Unit) {

  private var isShown = false

  init {
    dialog.findViewById<TextView>(R.id.yes).setOnClickListener { yes() }
    dialog.findViewById<TextView>(R.id.no).setOnClickListener { hide() }
  }

  fun setLayout(width: Int, height: Int): AlertFlashDialog {
    if (dialog.window != null) {
      dialog.window!!.setLayout(width, height)
      dialog.window!!.setBackgroundDrawable(
              ColorDrawable(Color.TRANSPARENT))
    }
    return this
  }

  fun setTitle(title: CharSequence): AlertFlashDialog {
    val titleView = dialog.findViewById<TextView>(R.id.disclaimerTitle)
    if (titleView != null)
      titleView.text = title
    return this
  }

  fun show() {
    if (!isShown)
      dialog.show()
    isShown = true
  }

  private fun hide() {
    if (isShown)
      dialog.hide()
    isShown = false
  }

  companion object {

    fun create(context: Context, root: ViewGroup, yes: () -> Unit): AlertFlashDialog {
      val dialog = Dialog(context)
      val dialogView = LayoutInflater.from(context)
              .inflate(R.layout.dialog_warning, root, false)
      dialog.setContentView(dialogView)
      if (dialog.window != null) {
        dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT))
      }
      return AlertFlashDialog(dialog, yes)
    }
  }
}