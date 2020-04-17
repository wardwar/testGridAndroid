package app.by.wildan.testgridandroid.data.remote

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException


abstract class NetworkConnectionInterceptor : Interceptor {

    abstract fun isInternetAvailable(): Boolean

    abstract fun onInternetUnavailable()

    abstract fun onConnectBackendAPIFailed()


    fun createResponse(statusCode: Int, message: String, chain: Interceptor.Chain): okhttp3.Response {
        return Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .code(statusCode)
            .message("network error")
            .body(
                """{"message": "$message"}"""
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .request(chain.request())
            .build()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        Timber.d("<<< Dipanggil, internet available = ${isInternetAvailable()}")
        val request = chain.request()
        if (!isInternetAvailable()) {
            onInternetUnavailable()
            return createResponse(404, "network error", chain)
        }
        try {
            return chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            onConnectBackendAPIFailed()
            return createResponse(404, "connect to API error", chain)
        } catch (e: UnknownHostException) {
            onConnectBackendAPIFailed()
            return createResponse(404, "cannot resolve API domain name", chain)
        }
    }
}