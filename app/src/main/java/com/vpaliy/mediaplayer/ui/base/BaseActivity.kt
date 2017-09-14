package com.vpaliy.mediaplayer.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import android.support.annotation.CallSuper
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    protected val disposables=CompositeDisposable()

    @Inject
    protected lateinit var eventBus: RxBus

    @Inject
    protected lateinit var navigator: Navigator

    abstract fun handleEvent(event: Any)
    abstract fun inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        inject()
        super.onCreate(savedInstanceState)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        disposables.add(eventBus.asFlowable()
                .subscribe({ this.processEvent(it) }))
    }

    private fun processEvent(`object`: Any?) {
        if (`object` != null) {
            handleEvent(`object`)
        }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        disposables.clear()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        disposables.clear()
    }
}