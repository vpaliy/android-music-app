package com.vpaliy.mediaplayer

infix fun <T> String?.ifNotEmpty(value:T)
        =if(isNullOrEmpty()) value else null

infix fun <T> Boolean.then(param:T):T?
        =if(this) param else null

fun <T> Boolean.then(result:T, default:T)
        =if (this) result else default

inline fun Boolean.then(expression:()->Unit)
        =if(this) expression() else {}

inline infix fun<T> Boolean.then(expression: () -> T)
        =if(this) expression() else null

inline fun<T> Boolean.then(expression:()->T, default:()->T)
        =if(this) expression() else default()