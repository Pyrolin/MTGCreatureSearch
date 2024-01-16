package com.example.mtgcreaturesearch.network

import CardApiService
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mtgcreaturesearch.Model.Data

class CardPagingSource(
     val cardApiService: CardApiService,
): PagingSource<Int, Data>() {

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val page = params.key ?: 1
            val response = cardApiService.getPhotos(page = page)
            val cards = response.data

            LoadResult.Page(
                data = response.data,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (response.data.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}