package com.vpaliy.mediaplayer.ui.base

import android.os.Handler
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Singleton

@Singleton
class RxBus {

    private val bus = PublishRelay.create<Any>().toSerialized()
    @Volatile private var isLocked: Boolean = false
    private val handler = Handler()

    fun send(o: Any) {
        bus.accept(o)
    }

    @JvmOverloads fun sendWithLock(event: Any, lockOutTime: Long = 500) {
        if (!isLocked) {
            isLocked = true
            send(event)
            handler.postDelayed({ isLocked = false }, lockOutTime)
        }
    }

    fun asFlowable(): Flowable<Any> {
        return bus.toFlowable(BackpressureStrategy.LATEST)
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }
}