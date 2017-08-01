package com.vpaliy.mediaplayer.ui;


import android.content.ComponentName;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.vpaliy.mediaplayer.MainActivity;
import com.vpaliy.mediaplayer.R;
import com.vpaliy.mediaplayer.media.service.MusicPlaybackService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;

public class PlayerFragment extends Fragment {

    private static final String TAG=PlayerFragment.class.getSimpleName();

    @BindView(R.id.background)
    protected ImageView background;

    @BindView(R.id.play_pause)
    protected ImageView playPause;

    @BindView(R.id.start_time)
    protected TextView startTime;

    @BindView(R.id.end_time)
    protected TextView endTime;

    @BindView(R.id.artist)
    protected TextView artist;

    @BindView(R.id.track_name)
    protected TextView trackName;

    private static final long PROGRESS_UPDATE_INTERNAL = 100;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 10;

    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;

    private PlaybackStateCompat lastState;
    private Handler handler=new Handler();

    @BindView(R.id.progressView)
    protected ProgressBar progressView;

    private MediaBrowserCompat browserCompat;
    private MediaBrowserCompat.ConnectionCallback connectionCallback=new MediaBrowserCompat.ConnectionCallback(){

        @Override
        public void onConnected()  {
            super.onConnected();
            MediaSessionCompat.Token token=browserCompat.getSessionToken();
            try {
                MediaControllerCompat mediaController =new MediaControllerCompat(getActivity(), token);
                // Save the controller
                mediaController.registerCallback(controllerCallback);
                MediaControllerCompat.setMediaController(getActivity(), mediaController);
                buildTransportUI();
            }catch (RemoteException ex){
                ex.printStackTrace();
            }


        }
    };

    private MediaControllerCompat.Callback controllerCallback=new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            lastState=state;
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            if(metadata!=null) updateDuration(metadata);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        browserCompat.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(MediaControllerCompat.getMediaController(getActivity())!=null){
            MediaControllerCompat.getMediaController(getActivity()).unregisterCallback(controllerCallback);
        }
        browserCompat.disconnect();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_full_player,container,false);
        ButterKnife.bind(this,root);
        browserCompat=new MediaBrowserCompat(getActivity(),
                new ComponentName(getActivity(), MusicPlaybackService.class),
                connectionCallback,null);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            Glide.with(getContext())
                    .load(R.drawable.evolve)
                    .asBitmap()
                    .priority(Priority.IMMEDIATE)
                    .into(new ImageViewTarget<Bitmap>(background) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            Blurry.with(getContext())
                                    .from(resource)
                                    .into(background);
                        }
                    });
        }
    }

    private void startSeekBarUpdate(){
        mScheduleFuture = mExecutorService.scheduleAtFixedRate(()-> handler.post(PlayerFragment.this::updateProgress),
                PROGRESS_UPDATE_INITIAL_INTERVAL, PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
    }

    private void stopSeekBarUpdate(){
        if(mScheduleFuture!=null) mScheduleFuture.cancel(false);
    }

    private void buildTransportUI(){

    }

    @OnClick(R.id.play_pause)
    public void playPause(){
        MediaControllerCompat controllerCompat=MediaControllerCompat.getMediaController(getActivity());
        PlaybackStateCompat stateCompat=controllerCompat.getPlaybackState();
        if(stateCompat!=null){
            MediaControllerCompat.TransportControls controls=
                    controllerCompat.getTransportControls();
            switch (stateCompat.getState()){
                case PlaybackStateCompat.STATE_PLAYING:
                case PlaybackStateCompat.STATE_BUFFERING:
                    playPause.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_play));
                    controls.pause();
                    stopSeekBarUpdate();
                    break;
                case PlaybackStateCompat.STATE_NONE:
                case PlaybackStateCompat.STATE_PAUSED:
                case PlaybackStateCompat.STATE_STOPPED:
                    playPause.setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.ic_pause));
                    controls.play();
                    startSeekBarUpdate();
                    break;
                default:
                    Log.d(TAG, "onClick with state "+stateCompat.getState());
            }
        }
    }

    @OnClick(R.id.next)
    public void playNext(){
        MediaControllerCompat controllerCompat=MediaControllerCompat.getMediaController(getActivity());
        MediaControllerCompat.TransportControls controls=
                controllerCompat.getTransportControls();
        controls.skipToNext();
    }

    @OnClick(R.id.prev)
    public void playPrev(){
        MediaControllerCompat controllerCompat=MediaControllerCompat.getMediaController(getActivity());
        MediaControllerCompat.TransportControls controls=
                controllerCompat.getTransportControls();
        controls.skipToPrevious();
    }

    private void updateDuration(MediaMetadataCompat metadataCompat){
        int duration=(int)metadataCompat.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
        endTime.setText(DateUtils.formatElapsedTime(duration/1000));
        startTime.setText("0");
        progressView.setMax(duration);
        //
        trackName.setText(metadataCompat.getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE));
        artist.setText(metadataCompat.getText(MediaMetadataCompat.METADATA_KEY_ARTIST));

    }

    private void updateProgress() {
        if(lastState==null) return;
        long currentPosition = lastState.getPosition();
        if (lastState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            long timeDelta = SystemClock.elapsedRealtime() -
                    lastState.getLastPositionUpdateTime();
            currentPosition += (int) timeDelta * lastState.getPlaybackSpeed();
        }
        progressView.setProgress((int) currentPosition);
        startTime.setText(DateUtils.formatElapsedTime(progressView.getProgress() / 1000));

    }
}
