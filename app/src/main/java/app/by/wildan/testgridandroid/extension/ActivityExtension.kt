package app.by.wildan.testgridandroid.extension

import android.app.Activity
import android.content.Intent

fun Activity.navigateAndClear(intent:Intent){
    startActivity(intent)
    finishAffinity()
}

fun Activity.navigate(intent:Intent){
    startActivity(intent)
}

fun Activity.navigateAndClear(cls: Class<*>){
    val intent = Intent(this,cls)
    navigateAndClear(intent)
}

fun Activity.navigate(cls: Class<*>){
    val intent = Intent(this,cls)
    navigate(intent)
}