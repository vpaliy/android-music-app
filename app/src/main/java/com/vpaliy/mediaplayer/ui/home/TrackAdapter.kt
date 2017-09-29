package com.vpaliy.mediaplayer.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.util.Pair
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
import com.vpaliy.mediaplayer.ui.utils.Packer
import kotlinx.android.synthetic.main.adapter_track_item.view.*

class TrackAdapter
constructor(context: Context, click:(Packer)->Unit, val clickMore:(Bundle)->Unit):BaseAdapter<Track>(context,click) {

    inner class TrackViewHolder constructor(itemView: View):
            BaseAdapter<Track>.GenericViewHolder(itemView) {

        val title:TextView=itemView.title
        val art:ImageView=itemView.art
        val artist:TextView=itemView.artist
        val duration:TextView=itemView.duration
        val transitionName:String=inflater.context.getString(R.string.art_trans)

        init{
            itemView.setOnClickListener {
                val queue=QueueManager(data,adapterPosition)
                val context=inflater.context;
                val bundle=BundleUtils.packHeavyObject(Bundle(),Constants.EXTRA_QUEUE,
                        queue,object:TypeToken<QueueManager>(){}.type)
                click(Packer(bundle, arrayOf(Pair(art,art.transitionName),
                        Pair(art,context.getString(R.string.background_trans)))))
            }
            itemView.more.setOnClickListener {
                val track=at(adapterPosition)
                clickMore.invoke(BundleUtils.packHeavyObject(Bundle(),Constants.EXTRA_TRACK,
                        track,object:TypeToken<Track>(){}.type))
            }
            itemView.setOnLongClickListener {
                itemView.more.performClick()
            }
        }
        @SuppressLint("SetTextI18n")
        override fun onBindData() {
            val track=at(adapterPosition)
            title.text=track.title
            artist.text=track.artist
            duration.text="\u2022 ${track.formatedDuration}"
            art.transitionName= transitionName +adapterPosition.toString()
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
