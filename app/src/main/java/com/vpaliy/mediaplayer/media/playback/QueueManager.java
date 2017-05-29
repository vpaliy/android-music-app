package com.vpaliy.mediaplayer.media.playback;


import android.support.annotation.NonNull;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import com.vpaliy.mediaplayer.media.model.MusicProvider;
import com.vpaliy.mediaplayer.media.utils.MediaHelper;
import com.vpaliy.mediaplayer.media.utils.QueueManagerUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QueueManager {

    private int currentIndex;
    private final MetadataUpdateListener updateListener;
    private final MusicProvider musicProvider;
    private List<MediaSessionCompat.QueueItem> audioQueue;

    public QueueManager(@NonNull MetadataUpdateListener listener,
                        @NonNull MusicProvider musicProvider){
        this.musicProvider=musicProvider;
        this.updateListener=listener;
        this.audioQueue= Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        this.currentIndex=0;
    }

    public void setCurrentIndex(int index){
        if(QueueManagerUtils.checkRange(audioQueue,currentIndex)){
            this.currentIndex=index;
            updateListener.onCurrentQueueIndexUpdated(index);
        }
    }

    public boolean skipQueuePosition(int amount){
        final int index=currentIndex+amount;
        this.currentIndex=index<0||audioQueue==null?index:index % audioQueue.size();
        return QueueManagerUtils.checkRange(audioQueue,currentIndex);
    }

    public MediaSessionCompat.QueueItem getCurrent(){
        return QueueManagerUtils.checkRange(audioQueue,currentIndex)
                ?audioQueue.get(currentIndex):null;
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
        MediaMetadataCompat metadata = musicProvider.byId(mediaId);
        if (metadata == null) {
            throw new IllegalArgumentException("Invalid musicId " + mediaId);
        }
        updateListener.onMetadataChanged(metadata);
        
    }

    public interface MetadataUpdateListener{
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
