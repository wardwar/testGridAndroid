package app.by.wildan.testgridandroid.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.by.wildan.testgridandroid.main.MainActivity
import app.by.wildan.testgridandroid.R
import app.by.wildan.testgridandroid.extension.navigate
import java.util.*
import kotlin.concurrent.schedule


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer("Splash Delay", false).schedule(3000L) {
                navigate(MainActivity::class.java)
            finish()
        }
    }
}
