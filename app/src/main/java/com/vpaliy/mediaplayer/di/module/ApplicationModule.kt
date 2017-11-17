package com.vpaliy.mediaplayer.di.module


import android.content.Context
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.executor.SchedulerProvider
import com.vpaliy.mediaplayer.ui.base.Navigator
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {
    @Singleton
    @Provides
    internal fun provideContext()=context

    @Singleton
    @Provides
    internal fun provideScheduler():BaseScheduler=SchedulerProvider()

    @Singleton
    @Provides
    internal fun provideNavigator()=Navigator()
}
