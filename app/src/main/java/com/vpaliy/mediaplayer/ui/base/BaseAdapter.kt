package com.vpaliy.mediaplayer.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import java.util.ArrayList
import android.view.ViewGroup
import com.vpaliy.mediaplayer.ui.utils.executeIf

abstract class BaseAdapter<T>
constructor(context: Context, val click:(Bundle)->Unit) :
        RecyclerView.Adapter<BaseAdapter<T>.GenericViewHolder>() {

    val inflater: LayoutInflater= LayoutInflater.from(context)
    var data:MutableList<T> =ArrayList()
        set(value) {
            field=value
            notifyDataSetChanged()
        }
    abstract inner class GenericViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun onBindData()
    }

    fun appendData(data: List<T>) {
        val size = itemCount
        this.data.addAll(data)
        notifyItemRangeInserted(size, itemCount)
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int)
            :Unit
            =holder.onBindData()

    fun clear()=executeIf(!data.isEmpty(),this::notifyDataSetChanged)

    override fun getItemCount()=data.size

    protected fun inflate(@LayoutRes id: Int, container: ViewGroup)
            :View
            =inflater.inflate(id, container, false)

    protected fun at(index: Int)=data[index]
}