package app.by.wildan.testgridandroid.data.repository.contentRepository

import app.by.wildan.testgridandroid.data.entity.BaseResponse
import app.by.wildan.testgridandroid.data.remote.ApiInterface
import io.reactivex.Observable


class ContentRepositoryImpl(
    private val apiService: ApiInterface
) : ContentRepository {
    override fun getImageContent(hashMap: HashMap<String, String>?): Observable<BaseResponse?> {
        return apiService.getImageContent(hashMap)

    }
}
