package app.by.wildan.testgridandroid.data.remote

import app.by.wildan.testgridandroid.data.entity.BaseResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap
import kotlin.collections.HashMap

interface ApiInterface {
    @GET("search/photos")
    fun getImageContent(@QueryMap hashMap: HashMap<String, String>?): Observable<BaseResponse?>

}