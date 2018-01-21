package com.vpaliy.mediaplayer.di.module

import android.content.Context
import com.vpaliy.mediaplayer.CLIENT_ID
import com.vpaliy.soundcloud.SoundCloud
import com.vpaliy.soundcloud.SoundCloudService
import com.vpaliy.soundcloud.model.Token
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import javax.inject.Singleton
import com.vpaliy.kotlin_extensions.info
import com.vpaliy.kotlin_extensions.then
import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Response
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit

@Module
class NetworkModule constructor(val token: Token?) {
  @Singleton
  @Provides
  internal fun service(context: Context): SoundCloudService =
      SoundCloud.create(CLIENT_ID)
          .appendToken(token)
          .createService(context)

  private class NetworkLoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
      val request = chain.request()
      val requestBody = request.body()
      val connection = chain.connection()
      val protocol = (connection != null) then connection.protocol() ?: Protocol.HTTP_1_1
      val requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol
      info(requestStartMessage)
      request.body()?.let {
        if (it.contentType() != null) {
          info("Content-Type: " + requestBody.contentType())
        }
        if (it.contentLength() != -1L) {
          info("Content-Length: " + requestBody.contentLength())
        }

        val headers = request.headers()
        (0..headers.size()).forEach {
          val name = headers.name(it)
          if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(name, ignoreCase = true))
            info(name + ": " + headers.value(it))
        }
        info("--> END " + request.method())
      }

      val startNs = System.nanoTime()
      val response = chain.proceed(request)
      return interceptResponse(response, endTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs))
    }

    private fun interceptResponse(response: Response, endTime: Long): Response {
      info("<-- " + response.code() + ' ' + response.message() + ' '
          + response.request().url() + " (" + endTime + "ms" + ")")
      val responseBody = response.body()
      val responseHeaders = response.headers()

      (0..responseHeaders.size()).forEach {
        info(responseHeaders.name(it) + ": " + responseHeaders.value(it))
      }

      if (!hasBody(response)) {
        info("<-- END HTTP")
      } else if (bodyEncoded(response.headers())) {
        info("<-- END HTTP (encoded body omitted)")
      } else if ("audio/x-m4a" == responseHeaders.get("Content-Type")) {
        info("<-- END HTTP (M4A body omitted)")
      } else if ("application/octet-stream" == responseHeaders.get("Content-Type")) {
        info("<-- END HTTP (octet-stream body omitted)")
      } else {
        val source = responseBody.source()
        source.request(java.lang.Long.MAX_VALUE)
        val buffer = source.buffer()

        var charset = UTF8
        val contentType = responseBody.contentType()
        if (contentType != null) {
          try {
            charset = contentType.charset(UTF8)
          } catch (e: UnsupportedCharsetException) {
            info("")
            info("Couldn't decode the response body; charset is likely malformed.")
            info("<-- END HTTP")
            return response
          }
        }

        if (contentLength(response.headers()) != 0L) {
          info(buffer.clone().readString(charset))
        }
        info("<-- END HTTP (" + buffer.size() + "-byte body)")
      }
      return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
      val contentEncoding = headers.get("Content-Encoding")
      return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    private val UTF8 = Charset.forName("UTF-8")

    private fun hasBody(response: Response) = when {
        response.request().method() == "HEAD" -> false
      else -> {
        val responseCode = response.code()
        (responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304 || contentLength(response) != -1L || "chunked"
            .equals(response.header("Transfer-Encoding"), ignoreCase = true)
      }
    }

    private fun contentLength(response: Response): Long {
      return contentLength(response.headers())
    }

    private fun contentLength(headers: Headers): Long {
      val length = headers.get("Content-Length")
      return (length != null) then length.toLong() ?: -1L
    }
  }
}
