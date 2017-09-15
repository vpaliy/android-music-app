package com.vpaliy.mediaplayer.ui.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.RxBus

class TrackAdapter(context: Context, rxBus: RxBus) : BaseAdapter<Track>(context, rxBus) {

    inner class TrackViewHolder internal constructor(itemView: View):
            BaseAdapter<Track>.GenericViewHolder(itemView) {

        val art: ImageView=itemView.findViewById(R.id.track_art) as ImageView
        val title:TextView=itemView.findViewById(R.id.track_title) as TextView
        val duration:TextView=itemView.findViewById(R.id.duration) as TextView
        val artist:TextView=itemView.findViewById(R.id.artist) as TextView

        override fun onBindData() {
            val track=at(adapterPosition)
            title.text=track.title
            artist.text=track.artist
            duration.text=track.formatedDuration
            Glide.with(inflater.context)
                    .load(track.artworkUrl)
                    .priority(Priority.IMMEDIATE)
                    .into(art)
        }
    }

    override fun onBindViewHolder(holder: BaseAdapter<Track>.GenericViewHolder, position: Int) =holder.onBindData()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            =TrackViewHolder(inflate(R.layout.adapter_track_item, parent))
}
