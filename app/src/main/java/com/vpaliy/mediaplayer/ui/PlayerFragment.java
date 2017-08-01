package com.vpaliy.mediaplayer.ui;


import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.vpaliy.mediaplayer.R;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;

public class PlayerFragment extends Fragment {

    @BindView(R.id.background)
    protected ImageView background;

    private static final long PROGRESS_UPDATE_INTERNAL = 1000;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 100;

    private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();

    private ScheduledFuture<?> mScheduleFuture;

    private PlaybackStateCompat stateCompat;
    private Handler handler=new Handler();

    @BindView(R.id.progressView)
    protected ProgressView progressView;

    private long lastTime;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.fragment_full_player,container,false);
        ButterKnife.bind(this,root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            stateCompat=new PlaybackStateCompat.Builder()
                    .setState(PlaybackStateCompat.STATE_PLAYING,0,1)
                    .build();
            lastTime=SystemClock.elapsedRealtime();
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
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(()-> handler.post(PlayerFragment.this::updateProgress),
                    PROGRESS_UPDATE_INITIAL_INTERVAL, PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void updateProgress() {
        progressView.setProgress(progressView.getProgress()+1);
    }
}
