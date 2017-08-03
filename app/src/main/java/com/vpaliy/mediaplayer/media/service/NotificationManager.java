package com.vpaliy.mediaplayer.media.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationManager {

    private static final int NOTIFICATION_ID = 412;
    private static final int REQUEST_CODE = 100;

    public static final String ACTION_PAUSE = "com.vpaliy.player.pause";
    public static final String ACTION_PLAY = "com.vpaliy.player.play";
    public static final String ACTION_PREV = "com.vpaliy.player.prev";
    public static final String ACTION_NEXT = "com.vpaliy.player.next";
    public static final String ACTION_STOP_CASTING = "com.vpaliy.player.stop_casting";


}
