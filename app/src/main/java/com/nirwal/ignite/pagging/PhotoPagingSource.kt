package com.nirwal.ignite.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.nirwal.ignite.domain.model.Photo
import com.nirwal.ignite.domain.repositry.PhotoRepository
import java.io.IOException


class PhotoPagingSource(private val repo:PhotoRepository, private val query:String):PagingSource<Int,Photo>() {
   private val STARTING_PAGE = 1

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE
        val itemLoadCount = params.loadSize

        return try {
            println(query)

            val response = when{
                "All"==query -> {repo.getCuratedPhotos(position, itemLoadCount)}
                else -> {repo.searchPhotos(query,position, itemLoadCount)}
            }

            println(query)
            //val response = repo.getCuratedPhotos(position, itemLoadCount)
            val photos = response?.photos
            val nextKey = if (photos.isNullOrEmpty()) {
                null
            } else {
                // initial load size = 3 * NETWORK_PAGE_SIZE
                // ensure we're not requesting duplicating items, at the 2nd request
                position + (params.loadSize / itemLoadCount)
            }

            LoadResult.Page(
                data = photos ?: listOf(),
                prevKey = if (position == STARTING_PAGE) null else position - 1,
                nextKey = nextKey
            )
        }catch (exception: IOException){
            return LoadResult.Error(exception)
        }catch (exception: HttpException){
            return LoadResult.Error(exception)
        }


    }
}