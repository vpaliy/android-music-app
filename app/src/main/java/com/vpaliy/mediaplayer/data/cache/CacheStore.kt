package com.vpaliy.mediaplayer.data.cache

import java.util.concurrent.ConcurrentMap
import io.reactivex.Single

class CacheStore<K, V>(private val cache: ConcurrentMap<K, V>) {

    fun invalidate(key: K)= cache.remove(key)

    fun put(key: K, value: V)= cache.put(key, value)

    fun putAll(m: Map<out K, V>)= cache.putAll(m)

    fun getStream(key: K): Single<V> {
        val value = cache[key]
        if (value != null) {
            return Single.just(value)
        }
        return Single.error<V>(IllegalArgumentException("Wrong key!"))
    }

    fun isInCache(key: K?)= key != null && cache[key] != null

    fun size()= cache.size.toLong()
}