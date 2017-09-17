package com.vpaliy.mediaplayer.ui.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import kotlinx.android.synthetic.main.adapter_track_item.view.*

class TrackAdapter(context: Context, click:(Track)->Unit) : BaseAdapter<Track>(context, click) {

    inner class TrackViewHolder constructor(itemView: View):
            BaseAdapter<Track>.GenericViewHolder(itemView) {

        val title:TextView=itemView.title
        val art:ImageView=itemView.art
        val artist:TextView=itemView.artist
        val duration:TextView=itemView.duration
        init{
            itemView.setOnClickListener {click(at(adapterPosition))}
        }
        override fun onBindData() {
            val track=at(adapterPosition)
            title.text=track.title
            artist.text=track.artist
            duration.text=track.formatedDuration
            Glide.with(itemView.context)
                    .load(track.artworkUrl)
                    .placeholder(R.drawable.placeholder)
                    .animate(R.anim.fade_in)
                    .into(art)
        }
    }

    override fun onBindViewHolder(holder: BaseAdapter<Track>.GenericViewHolder, position: Int) =holder.onBindData()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=TrackViewHolder(inflate(R.layout.adapter_track_item, parent))
}
