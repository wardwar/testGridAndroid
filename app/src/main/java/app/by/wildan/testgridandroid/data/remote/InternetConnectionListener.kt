package app.by.wildan.testgridandroid.data.remote

interface InternetConnectionListener {
    fun onInternetUnavailable()
    fun onConnectAPIFailed()
}