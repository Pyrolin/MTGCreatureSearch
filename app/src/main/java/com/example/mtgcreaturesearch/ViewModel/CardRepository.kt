package com.example.mtgcreaturesearch.ViewModel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class CardRepository @Inject constructor(
    private val remoteDataSource: CardViewModel
) {

    fun getCards() = Pager(
        config = PagingConfig(pageSize = 175, prefetchDistance = 1),
        pagingSourceFactory = {
            CardPagingSource(remoteDataSource)
        }
    ).flow
}