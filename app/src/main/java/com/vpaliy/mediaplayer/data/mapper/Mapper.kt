package com.vpaliy.mediaplayer.data.mapper

abstract class Mapper<R,F>{
    fun reverse(list:List<R>?):List<F>? {
        return list?.let {
            val result = arrayListOf<F>()
            list.forEach({
                val element = reverse(it)
                element?.let { result.add(element) }
            })
            return result
        }
    }
    fun map(list:List<F>?):List<R>?{
        return list?.let {
            val result= arrayListOf<R>()
            list.forEach({
                val element=map(it)
                element?.let{result.add(element)}
            })
            return result
        }
    }
    abstract fun map(fake:F?):R?
    abstract fun reverse(real:R?):F?
}