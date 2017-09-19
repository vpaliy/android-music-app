package com.vpaliy.mediaplayer.ui.utils

import android.os.Bundle
import com.google.gson.Gson
import java.io.InputStreamReader
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.reflect.Type
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

object BundleUtils {

    fun compress(data: String?): ByteArray? {
       return data?.let {
            val bos = ByteArrayOutputStream(data.length)
            try {
                val gzip = GZIPOutputStream(bos)
                gzip.write(data.toByteArray())
                gzip.close()
                val compressed = bos.toByteArray()
                bos.close()
                return compressed
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
           return null
        }
    }

    fun decompress(compressed: ByteArray?): String? {
        return compressed?.let {
            val bis = ByteArrayInputStream(compressed)
            try {
                val gis = GZIPInputStream(bis)
                val br = BufferedReader(InputStreamReader(gis, "UTF-8"))
                val sb = StringBuilder()
                var line=br.readLine()
                while (line != null) {
                    sb.append(line)
                    line=br.readLine()
                }
                br.close()
                gis.close()
                bis.close()
                return sb.toString()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

            return null
        }
    }

    fun <T> convertFromJsonString(jsonString: String?, type: Type)=jsonString?.let {
        Gson().fromJson<T>(jsonString, type)
    }

    fun convertToJsonString(`object`: Any?, type: Type)= `object`?.let {
            Gson().toJson(`object`,type)
    }

    fun packHeavyObject(bundle: Bundle, key: String, `object`: Any, type: Type):Bundle {
        val jsonString = convertToJsonString(`object`, type)
        val compressedStuff = compress(jsonString)
        bundle.putByteArray(key, compressedStuff)
        return bundle
    }

    fun <T> fetchHeavyObject(type: Type?, bundle: Bundle?, key: String): T? {
        if (bundle != null && !key.isNullOrEmpty() && type != null) {
            val compressedStuff = bundle.getByteArray(key)
            val jsonString = decompress(compressedStuff)
            if (!jsonString.isNullOrEmpty()) {
                return convertFromJsonString<T>(jsonString, type)
            }
        }
        return null
    }
}