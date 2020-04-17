package app.by.wildan.testgridandroid

import app.by.wildan.testgridandroid.data.remote.ApiService
import app.by.wildan.testgridandroid.data.repository.contentRepository.ContentRepository
import app.by.wildan.testgridandroid.data.repository.contentRepository.ContentRepositoryImpl
import app.by.wildan.testgridandroid.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    factory { ApiService.apiService(androidContext()) }
    factory { ContentRepositoryImpl(get()) as ContentRepository }
    viewModel { MainViewModel(get()) }


}