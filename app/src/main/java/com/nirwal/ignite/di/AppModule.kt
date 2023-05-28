package com.nirwal.ignite.di

import com.nirwal.ignite.common.MyDataStore
import com.nirwal.ignite.domain.database.FavPhotoDatabase
import com.nirwal.ignite.domain.repositry.PhotoRepository
import com.nirwal.ignite.domain.repositry.PhotoRepositoryImp
import com.nirwal.ignite.ui.viewModel.MainViewModel
import com.nirwal.ignite.ui.viewModel.SettingViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<PhotoRepository> { PhotoRepositoryImp()  }
    single { FavPhotoDatabase.getInstance(androidApplication()) }
    single { get<FavPhotoDatabase>().photoDao() }

    single { MyDataStore(androidContext()) }

    viewModel { MainViewModel(androidContext(),get(),get()) }
    viewModel { SettingViewModel(get(),get())}
}