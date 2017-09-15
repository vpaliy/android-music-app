package com.vpaliy.mediaplayer.ui.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.RxBus

class TrackAdapter(context: Context, rxBus: RxBus) : BaseAdapter<Track>(context, rxBus) {

    inner class TrackViewHolder internal constructor(itemView: View):
            BaseAdapter<Track>.GenericViewHolder(itemView) {

        override fun onBindData() {

        }
    }

    override fun onBindViewHolder(holder: BaseAdapter<Track>.GenericViewHolder, position: Int) =holder.onBindData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            =TrackViewHolder(inflate(R.layout.adapter_track_item, parent))
}
