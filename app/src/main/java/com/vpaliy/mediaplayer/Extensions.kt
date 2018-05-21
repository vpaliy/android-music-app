package com.vpaliy.mediaplayer

import android.content.ComponentCallbacks
import android.support.v4.widget.SwipeRefreshLayout
import io.reactivex.Single
import org.koin.android.ext.android.inject

infix fun <T> String?.ifNotEmpty(value: T)
    = if (isNullOrEmpty()) value else null

infix fun <T> Boolean.then(param: T): T?
    = if (this) param else null

fun <T> Boolean.then(result: T, default: T)
    = if (this) result else default

inline fun Boolean.then(expression: () -> Unit)
    = if (this) expression() else {
}

inline infix fun <T> Boolean.then(expression: () -> T)
    = if (this) expression() else null

inline fun <T> Boolean.then(expression: () -> T, default: () -> T)
    = if (this) expression() else default()

fun <T> wrongArgument(): Single<T> = Single.error(IllegalArgumentException())

inline infix fun <reified T> ComponentCallbacks.injectWith(name: String)
    : Lazy<T> = inject(name) { mapOf(name to this) }

fun SwipeRefreshLayout.stopRefreshing() {
  isRefreshing = false
}

fun SwipeRefreshLayout.startRefreshing() {
  isRefreshing = false
}
