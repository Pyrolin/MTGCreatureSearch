package com.example.mtgcreaturesearch.ViewModel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import javax.inject.Inject

class CardRepository @Inject constructor() {

    fun getCards() = Pager(
        config = PagingConfig(pageSize = 175, prefetchDistance = 125),
        pagingSourceFactory = {
            CardPagingSource()
        }
    ).flow
}