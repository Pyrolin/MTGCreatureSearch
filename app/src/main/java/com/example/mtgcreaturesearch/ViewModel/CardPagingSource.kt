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
import java.io.IOException

class CardPagingSource(
    private val remoteDataSource: CardViewModel,
) : PagingSource<Int, ShownCards>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShownCards> {
        Log.d("TEST", params.key.toString())

        return try {
            val currentPage = params.key ?: 1
            val cards = remoteDataSource.getCards(
                page = currentPage
            )

            LoadResult.Page(
                data = cards,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (cards.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ShownCards>): Int? {
        TODO("Not yet implemented")
    }
}