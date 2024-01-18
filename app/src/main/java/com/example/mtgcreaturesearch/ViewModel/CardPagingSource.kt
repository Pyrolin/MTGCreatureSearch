package com.example.mtgcreaturesearch.ViewModel

import CardApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mtgcreaturesearch.Model.Data
import java.io.IOException

class CardPagingSource() : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        return try {
            val currentPage = params.key ?: 1
            val cards = CardApi.retrofitService.getPhotos(
                q = currentQuery.q,
                page = currentPage.toString()
            )

            LoadResult.Page(
                data = cards.data,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (cards.data.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: retrofit2.HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        TODO("Not yet implemented")
    }
}