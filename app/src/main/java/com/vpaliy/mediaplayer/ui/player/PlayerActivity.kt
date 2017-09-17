package com.vpaliy.mediaplayer.ui.player

import android.content.ComponentName
import android.os.Bundle
import android.os.Handler
import android.os.RemoteException
import android.os.SystemClock
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.widget.SeekBar
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.playback.PlaybackManager
import com.vpaliy.mediaplayer.ui.base.BaseActivity
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import javax.inject.Inject
import com.vpaliy.mediaplayer.playback.MusicPlaybackService
import kotlinx.android.synthetic.main.activity_player.*
import java.util.concurrent.TimeUnit


class PlayerActivity: BaseActivity(){

    private val executorService= Executors.newSingleThreadScheduledExecutor()
    private var scheduledFuture: ScheduledFuture<*>? = null

    private val handler= Handler()
    private val PROGRESS_UPDATE_INTERNAL: Long = 100
    private val PROGRESS_UPDATE_INITIAL_INTERVAL: Long = 10
    private var lastState:PlaybackStateCompat?=null

    private val controllerCallback=object:MediaControllerCompat.Callback(){
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)
        }
        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            super.onMetadataChanged(metadata)
        }
    }

    private val connectionCallback:MediaBrowserCompat.ConnectionCallback=object:MediaBrowserCompat.ConnectionCallback(){
        override fun onConnected() {
            super.onConnected()
            try {
                val mediaController = MediaControllerCompat(this@PlayerActivity, browser.sessionToken)
                mediaController.registerCallback(controllerCallback)
                MediaControllerCompat.setMediaController(this@PlayerActivity, mediaController)
                inject()
            } catch (ex: RemoteException) {
                ex.printStackTrace()
            }

        }
    }

    private val browser= MediaBrowserCompat(this,
            ComponentName(this, MusicPlaybackService::class.java),
            connectionCallback, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_full_player)
        supportPostponeEnterTransition()
        progressView.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onStopTrackingTouch(seekBar: SeekBar?){
                seekBar?.let {
                    MediaControllerCompat.getMediaController(this@PlayerActivity).transportControls
                            .seekTo(it.progress.toLong())
                    startSeekBarUpdate()
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?)=stopSeekBarUpdate()
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                start_time.text = DateUtils.formatElapsedTime((progress / 1000).toLong())
            }
        })
    }

    override fun onStart() {
        super.onStart()
        browser.connect()
    }

    override fun onStop() {
        super.onStop()
        browser.disconnect()
        MediaControllerCompat.getMediaController(this)
                .unregisterCallback(controllerCallback)
    }

    private fun stopSeekBarUpdate(){
        lastState=null
        scheduledFuture?.cancel(true)
    }

    private fun startSeekBarUpdate(){
        scheduledFuture = executorService.scheduleAtFixedRate({handler.post(this@PlayerActivity::updateProgress)},
                PROGRESS_UPDATE_INITIAL_INTERVAL, PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS)
    }

    private fun updateProgress(){
        lastState?.let {
            var position=it.position
            if (it.state ==PlaybackStateCompat.STATE_PLAYING) {
                val timeDelta = SystemClock.elapsedRealtime() - it.lastPositionUpdateTime
                position+=(timeDelta.toInt() * it.playbackSpeed).toLong()
            }
            progressView.progress=position.toInt()
            start_time.text=DateUtils.formatElapsedTime(position/1000)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        stopSeekBarUpdate()
        executorService.shutdown()
    }

    override fun inject() =FitnessSound.app().
            playbackComponent().inject(this)

    @Inject
    fun injectManager(manager:PlaybackManager){

    }
}
