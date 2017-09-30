package com.vpaliy.mediaplayer.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import java.util.ArrayList
import android.view.ViewGroup
import com.vpaliy.mediaplayer.ui.utils.Packer

abstract class BaseAdapter<T>(context: Context, protected val click:(Packer)->Unit) :
        RecyclerView.Adapter<BaseAdapter<T>.GenericViewHolder>() {

    protected var data:MutableList<T> =ArrayList<T>()
    protected val inflater: LayoutInflater= LayoutInflater.from(context)

    abstract inner class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        abstract fun onBindData()
    }

    fun set(data:MutableList<T>){
        this.data=data
        notifyDataSetChanged()
    }

    fun appendData(data: List<T>) {
        val size = itemCount
        this.data.addAll(data)
        notifyItemRangeInserted(size, itemCount)
    }

    fun addItem(item: T): BaseAdapter<T> {
        val size = itemCount
        data.add(item)
        notifyItemRangeInserted(size, itemCount)
        return this
    }

    fun clear(){
        if(!data.isEmpty()) {
            data.clear()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount()=data.size

    protected fun inflate(@LayoutRes id: Int, container: ViewGroup):View
            =inflater.inflate(id, container, false)

    protected fun at(index: Int)=data[index]
}