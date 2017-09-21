package com.vpaliy.mediaplayer.ui.home

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.reflect.TypeToken
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.QueueManager
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.utils.BundleUtils
import com.vpaliy.mediaplayer.ui.utils.Constants
import kotlinx.android.synthetic.main.adapter_track_item.view.*

class TrackAdapter(context: Context, click:(Bundle)->Unit) : BaseAdapter<Track>(context,click) {

    inner class TrackViewHolder constructor(itemView: View):
            BaseAdapter<Track>.GenericViewHolder(itemView) {

        val title:TextView=itemView.title
        val art:ImageView=itemView.art
        val artist:TextView=itemView.artist
        init{
            itemView.setOnClickListener {
                val queue=QueueManager(data,adapterPosition)
                click(BundleUtils.packHeavyObject(Bundle(),Constants.EXTRA_QUEUE,
                        queue,object:TypeToken<QueueManager>(){}.type))
            }
        }
        override fun onBindData() {
            val track=at(adapterPosition)
            title.text=track.title
            artist.text=track.artist
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
