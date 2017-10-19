package com.vpaliy.mediaplayer.ui.utils

import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import com.vpaliy.mediaplayer.then

inline fun <T:View> T.click(crossinline block:()->Unit)=setOnClickListener{ block() }

infix fun <T:View> T.getString(resource:Int):String=context.getString(resource)

fun <T:View> T.showMessage(resource:Int,duration:Int=2000)
        =Snackbar.make(this,resource,duration).show()

fun <T:TextView> T.assignTextIf(condition: Boolean, first:Int, second:Int){
    text=condition.then(getString(first),getString(second))
}