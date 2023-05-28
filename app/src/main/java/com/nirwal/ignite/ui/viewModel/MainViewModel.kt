package com.nirwal.ignite.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.nirwal.ignite.common.Utils
import com.nirwal.ignite.domain.database.FavouritePhotoDao
import com.nirwal.ignite.domain.database.PhotoEntity
import com.nirwal.ignite.domain.database.SearchHistoryDao
import com.nirwal.ignite.domain.database.SearchHistoryEntity
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.repositry.PhotoRepository
import com.nirwal.ignite.pagging.PhotoPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val context:Context,
    private val repository: PhotoRepository,
    private val searchHistoryDb:SearchHistoryDao,
    private val db:FavouritePhotoDao
):ViewModel() {

    private val PAGE_SIZE = 90

    private val _imagesSource = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val imageSource= _imagesSource

    val favoritePhotos = db.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val searchHistory = searchHistoryDb.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    init {
     onSearch("All")
    }

    fun updateFavourites(photo:PhotoEntity, isFavourite:Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (isFavourite){
                    db.upsert(photo)
                }else{
                    db.delete(photo)
                }
            }
        }
    }

    fun getPhoto(id:Int)= flow {
        val a= repository.getPhoto(id = id)
        if(a!= null) {
            emit(a)
        }

    }.flowOn(Dispatchers.IO)

    fun onSearch(query:String){
        viewModelScope.launch {
            Pager(PagingConfig(pageSize = PAGE_SIZE)) {
                PhotoPagingSource(repository,query)
            }.flow.cachedIn(viewModelScope).collect{
                _imagesSource.value =
                    it
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                searchHistoryDb.upsert(SearchHistoryEntity(query = query))
            }
        }
    }


    fun setWallpaper(imageUr:String){
        Utils.setWallpaper(imageUr, context)
        Toast.makeText(context, "Wallpaper updated successfully", Toast.LENGTH_LONG).show()
    }

    fun setLockScreen(imageUr:String){
        Utils.setLockScreen(imageUr, context)
        Toast.makeText(context, "Lock Screen updated successfully", Toast.LENGTH_LONG).show()
    }



}


