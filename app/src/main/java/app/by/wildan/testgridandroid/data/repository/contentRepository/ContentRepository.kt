package app.by.wildan.testgridandroid.data.repository.contentRepository


import app.by.wildan.testgridandroid.data.entity.BaseResponse
import io.reactivex.Observable

interface ContentRepository {
    fun getImageContent(hashMap: HashMap<String, String>?): Observable<BaseResponse?>
}