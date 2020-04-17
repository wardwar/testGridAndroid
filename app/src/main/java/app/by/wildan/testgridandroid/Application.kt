package app.by.wildan.testgridandroid

import android.app.Application
import app.by.wildan.testgridandroid.data.remote.InternetConnectionListener
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


class Application : Application() {
    var mInternetConnectionListener: InternetConnectionListener? = null

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@Application)
            modules(appModule)
        }

    }


}