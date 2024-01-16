package com.example.mtgcreaturesearch.ViewModel

import CardApi
import CardApiService
import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val remoteDataSource: CardApi
) {

    fun getCards() = Pager(
        config = PagingConfig(pageSize = 175, prefetchDistance = 125),
        pagingSourceFactory = {
            CardPagingSource(remoteDataSource)
        }
    ).flow
}