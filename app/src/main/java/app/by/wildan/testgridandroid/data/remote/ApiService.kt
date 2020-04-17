package app.by.wildan.testgridandroid.data.remote


import android.content.Context
import app.by.wildan.testgridandroid.Application
import app.by.wildan.testgridandroid.BuildConfig
import app.by.wildan.testgridandroid.utils.isNetworkAvailable
import okhttp3.Dispatcher
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit


object ApiService {

    fun apiService(
        context: Context
    ): ApiInterface {
        var app = context.applicationContext as Application

        // timeout (second)
        var CONNECT_TIMEOUT: Long = 30
        var WRITE_TIMEOUT: Long = 30
        var READ_TIMEOUT: Long = 30

        if (BuildConfig.DEBUG) {
            CONNECT_TIMEOUT = 30
            WRITE_TIMEOUT = 30
            READ_TIMEOUT = 30
        }

        val PROTOCOLS = listOf(Protocol.HTTP_1_1, Protocol.HTTP_2)
        val MAX_REQUEST_PER_HOST = 500

        val endpoint = BuildConfig.API_URL
        val dispatcher = Dispatcher()
        dispatcher.maxRequestsPerHost = MAX_REQUEST_PER_HOST

        // logger
        val logger = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        })

        logger.level = HttpLoggingInterceptor.Level.BODY

        // network state interceptor
        val interceptorNetwork = object : NetworkConnectionInterceptor() {
            override fun onConnectBackendAPIFailed() {
                Timber.d("<<< onResolveDomain failed")
                app.mInternetConnectionListener?.onConnectAPIFailed()
            }

            override fun isInternetAvailable(): Boolean {
                return isNetworkAvailable(context)
            }

            override fun onInternetUnavailable() {
                Timber.d("<<< onInternetUnavailable failed ${app.mInternetConnectionListener}")
                app.mInternetConnectionListener?.onInternetUnavailable()
            }

        }

        // http client
        val client = OkHttpClient.Builder()

            .addInterceptor(interceptorNetwork)
            .addInterceptor(logger)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .dispatcher(dispatcher)
            .retryOnConnectionFailure(true)
            .protocols(PROTOCOLS)

        client.addInterceptor { chain ->
            val headers =
                Headers.Builder().add("Authorization", "Client-ID ${BuildConfig.API_KEY}").build()
            val original = chain.request()
            val request = original.newBuilder()
                .headers(headers)
                .method(original.method, original.body)
            chain.proceed(request.build())
        }

        // retrofit client
        val retrofit = Retrofit.Builder()
            .baseUrl(endpoint)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}