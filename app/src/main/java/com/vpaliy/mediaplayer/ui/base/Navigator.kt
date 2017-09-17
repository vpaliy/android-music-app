package com.vpaliy.mediaplayer.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.search.SearchActivity
import javax.inject.Inject
import javax.inject.Singleton
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair


@Singleton
class Navigator @Inject constructor(){

    fun navigate(activity: Activity, track: Track){

    }

    fun search(activity:Activity, pair:Pair<View,String>){
        val intent= Intent(activity,SearchActivity::class.java)
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            val optionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pair)
            activity.startActivity(intent, optionsCompat.toBundle())
        }else activity.startActivity(intent)
    }
}
