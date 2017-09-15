package com.vpaliy.mediaplayer.di.module


import android.content.Context
import com.vpaliy.mediaplayer.domain.executor.BaseScheduler
import com.vpaliy.mediaplayer.domain.executor.SchedulerProvider
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.base.RxBus
import javax.inject.Singleton
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val context: Context) {
    @Singleton
    @Provides
    fun provideContext()=context

    @Singleton
    @Provides
    fun provideScheduler():BaseScheduler=SchedulerProvider()

    @Singleton
    @Provides
    fun provideBus()=RxBus()

    @Singleton
    @Provides
    fun provideNavigator()=Navigator()
}
