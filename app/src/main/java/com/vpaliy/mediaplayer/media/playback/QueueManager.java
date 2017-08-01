package com.vpaliy.mediaplayer.media.playback;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class QueueManager {

    private List<MediaMetadataCompat> queueData;
    private int index;

    public QueueManager(){
        queueData=new LinkedList<>();
    }

    public void add(MediaMetadataCompat metadata){
        queueData.add(metadata);
    }

    public void setQueueData(List<MediaMetadataCompat> queueData) {
        this.queueData = queueData;
    }

    public MediaMetadataCompat next(){
        if((index+1)<queueData.size()){
            return queueData.get(++index);
        }
        return null;
    }

    public MediaMetadataCompat previous(){
        if(index==0||(index-1)>=queueData.size())
            return null;
        return queueData.get(--index);
    }

    public MediaMetadataCompat current(){
        return !queueData.isEmpty()?queueData.get(index):null;
    }
}
