package com.vpaliy.mediaplayer.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vpaliy.mediaplayer.R;

public class AlertFlashDialog {
    private final Dialog dialog;
    private boolean isShown = false;

    private AlertFlashDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public static AlertFlashDialog create(Context context, ViewGroup root) {
        final Dialog dialog = new Dialog(context);
        final View dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_warning, root, false);
        dialog.setContentView(dialogView);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        return new AlertFlashDialog(dialog);
    }

    public AlertFlashDialog setLayout(int width, int height) {
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(Color.TRANSPARENT));
        }
        return this;
    }

    public AlertFlashDialog setTitle(@StringRes int title) {
        return setTitle(dialog.getContext().getString(title));
    }

    public AlertFlashDialog setTitle(CharSequence title) {
        final TextView titleView = dialog.findViewById(R.id.disclaimerTitle);
        if (titleView != null)
            titleView.setText(title);
        return this;
    }

    public void show() {
        if (!isShown)
            dialog.show();
        isShown = true;
    }

    public void hide() {
        if (isShown)
            dialog.hide();
        isShown = false;
    }
}
