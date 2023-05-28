package com.nirwal.ignite.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.nirwal.ignite.common.Utils
import com.nirwal.ignite.domain.database.FavouritePhotoDao
import com.nirwal.ignite.domain.database.PhotoEntity
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.repositry.PhotoRepository
import com.nirwal.ignite.pagging.PhotoPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainViewModel(
    private val context:Context,
    private val repository: PhotoRepository,
    private val db:FavouritePhotoDao
):ViewModel() {

    val PAGE_SIZE = 90

    var imagesSource = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
        PhotoPagingSource(repository,"All")
    }.flow


    val favoritePhotos = flow{
        emit(db.getAll())
    }.flowOn(Dispatchers.IO)

    fun updateFavourites(photo:PhotoEntity, isFavourite:Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                if (isFavourite){
                    db.insert(photo)
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



    fun onFilterChange(filter:String){
        println(filter)
        imagesSource  = Pager(PagingConfig(pageSize = PAGE_SIZE)) {
            PhotoPagingSource(repository,filter)
        }.flow
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