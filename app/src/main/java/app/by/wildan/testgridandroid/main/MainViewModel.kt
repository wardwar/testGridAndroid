package app.by.wildan.testgridandroid.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.by.wildan.testgridandroid.Event
import app.by.wildan.testgridandroid.data.entity.BaseResponse
import app.by.wildan.testgridandroid.data.repository.contentRepository.ContentRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val contentRepository: ContentRepository) : ViewModel() {
    private val orientation = "portrait"
    private val perPage = 12

    val categoryList = listOf(
        "Food",
        "Muslim",
        "Hijab",
        "Book",
        "Nature",
        "Animal",
        "Office"
    )


    val disposable: CompositeDisposable by lazy { CompositeDisposable() }

    private val _contentResponse = MutableLiveData<BaseResponse>()
    val contentResponse: LiveData<BaseResponse> = _contentResponse

    private val _loadingPerform = MutableLiveData<Event<Boolean>>()
    val loadingPerform: LiveData<Event<Boolean>> = _loadingPerform

    private val _errorHandle = MutableLiveData<Event<String>>()
    val errorHandle: LiveData<Event<String>> = _errorHandle

    fun getContent(hashMap: HashMap<String, String>) {
        hashMap["per_page"] = perPage.toString()
        hashMap["orientation"] = orientation
        disposable.add(
            contentRepository.getImageContent(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _loadingPerform.value = Event(true) }
                .doOnComplete { _loadingPerform.value = Event(false) }
                .subscribe({

                    _contentResponse.value = it
                }, {
                    _errorHandle.value = Event(it.localizedMessage)
                })
        )

    }

    fun onDestroy() {
        disposable.dispose()
    }

}