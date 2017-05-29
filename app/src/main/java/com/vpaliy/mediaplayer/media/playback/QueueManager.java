package com.vpaliy.mediaplayer.media.playback;


import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.vpaliy.mediaplayer.media.utils.MediaHelper;
import com.vpaliy.mediaplayer.media.utils.QueueManagerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueManager {

    private int currentIndex;
    private MetadataUpdateListener updateListener;
    private List<MediaSessionCompat.QueueItem> audioQueue;

    public QueueManager(@NonNull MetadataUpdateListener listener){
        this.updateListener=listener;
        this.audioQueue= Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        this.currentIndex=0;
    }

    public void setCurrentIndex(int index){
        if(checkForRange(index)){
            this.currentIndex=index;
            updateListener.onCurrentQueueIndexUpdated(index);
        }
    }

    public boolean skipQueuePosition(int amount){
        final int index=currentIndex+amount;
        this.currentIndex=index<0||audioQueue==null?index:index % audioQueue.size();
        return checkForRange(index);
    }

    public MediaSessionCompat.QueueItem getCurrent(){
        return checkForRange(currentIndex)?audioQueue.get(currentIndex):null;
    }

    public int size(){
        return audioQueue!=null?audioQueue.size():0;
    }

    public boolean setCurrentItem(long mediaId){
        int index= QueueManagerUtils.getIndex(audioQueue,mediaId);
        setCurrentIndex(index);
        return index>=0;
    }

    public boolean setCurrentItem(String mediaId){
        int index= QueueManagerUtils.getIndex(audioQueue,mediaId);
        setCurrentIndex(index);
        return index>=0;
    }

    public void updateMetadata(){
        MediaSessionCompat.QueueItem currentItem=getCurrent();
        if(currentItem==null){
            updateListener.onMetadataRetrieveError();
            return;
        }
        final String mediaId= MediaHelper.createMediaID(currentItem.getDescription().getMediaId());


    }

    private boolean checkForRange(int index){
        return !(index<0||audioQueue==null||audioQueue.size()<=index);
    }


    public interface MetadataUpdateListener{
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
