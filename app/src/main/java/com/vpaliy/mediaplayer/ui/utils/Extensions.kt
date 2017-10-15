package com.vpaliy.mediaplayer.ui.utils

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView

inline fun <T:View> T.click(crossinline block:()->Unit)=setOnClickListener{ block() }

inline fun Any.executeIf(condition:Boolean, expression:()->Unit, default:()->Unit)
        :Unit=if(condition) expression() else default()

inline fun Any.executeIf(condition:Boolean, expression:()->Unit)=if(condition) expression() else {}

inline fun<T> List<T>?.ifNullOrEmpty(default: () -> Unit,expression: () -> Unit)=
        if(this==null || isEmpty()) default() else expression()

fun <T:View> T.showMessage(resource:Int,duration:Int=2000)=Snackbar.make(this,resource,duration).show()

fun <T> Any.returnIf(condition:Boolean,first:T, second:T)=if(condition) first else second

fun <T> String?.returnIfNotEmpty(value:T)=if(this.isNullOrEmpty()) value else null

fun <T:View> T.getString(resource:Int):String=context.getString(resource)

fun <T:TextView> T.assignTextIf(condition: Boolean, first:Int, second:Int){
    text=returnIf(condition,getString(first),getString(second))
}