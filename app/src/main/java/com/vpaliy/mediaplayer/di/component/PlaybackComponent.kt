package com.vpaliy.mediaplayer.di.component

import com.vpaliy.mediaplayer.di.module.MediaPlayerModule
import com.vpaliy.mediaplayer.playback.MusicPlaybackService
import com.vpaliy.mediaplayer.ui.player.PlayerActivity
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope
import dagger.Component

@PlaybackScope
@Component(dependencies = [(ApplicationComponent::class)], modules = [(MediaPlayerModule::class)])
interface PlaybackComponent {
  fun inject(service: MusicPlaybackService)
  fun inject(activity: PlayerActivity)
}
