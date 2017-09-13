package com.vpaliy.mediaplayer.data.mapper

abstract class Mapper<R,F>{

    fun reverse(list:List<R>?):List<F?>?{
        if(list!=null){
            val result= arrayListOf<F?>()
            list.forEach({element->result.add(reverse(element))})
            return result
        }
        return null
    }

    fun map(list:List<F>?):List<R?>?{
        if(list!=null){
            val result= arrayListOf<R?>()
            list.forEach({element->result.add(map(element))})
            return result
        }
        return null
    }

    abstract fun map(fake:F?):R?
    abstract fun reverse(real:R?):F?
}