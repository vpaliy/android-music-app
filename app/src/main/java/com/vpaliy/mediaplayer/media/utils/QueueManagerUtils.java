package com.vpaliy.mediaplayer.media.utils;


import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

public class QueueManagerUtils {

    public static int getIndex(Iterable<MediaSessionCompat.QueueItem> queue, String mediaId){
        int index=0;
        for(MediaSessionCompat.QueueItem item:queue){
            if(mediaId.equals(item.getDescription().getMediaId())){
                return index;
            }
            index++;
        }
        return -1;
    }

    public static boolean checkRange(List<MediaSessionCompat.QueueItem> queue, int index){
        return !(index<0||queue==null||queue.size()<=index);
    }

    public static int getIndex(Iterable<MediaSessionCompat.QueueItem> queue, long queueId){
        int index=0;
        for(MediaSessionCompat.QueueItem item:queue){
            if (queueId == item.getQueueId()) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
