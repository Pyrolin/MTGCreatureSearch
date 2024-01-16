package com.example.mtgcreaturesearch.ViewModel

import CardApi
import CardApiService
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.Model.Query
import com.example.mtgcreaturesearch.Model.ShownCards
import com.example.mtgcreaturesearch.View.test_query
import java.io.IOException

class CardPagingSource(
    private val remoteDataSource: CardApi,
) : PagingSource<Int, Data>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        Log.d("TEST", params.key.toString())

        return try {
            val currentPage = params.key ?: 1
            val cards = CardApi.retrofitService.getPhotos(
                q = test_query.q,
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