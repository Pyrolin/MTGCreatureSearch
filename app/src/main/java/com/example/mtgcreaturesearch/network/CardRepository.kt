package com.example.mtgcreaturesearch.network

import CardApiService
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mtgcreaturesearch.Model.Card
import com.example.mtgcreaturesearch.Model.Data
import com.example.mtgcreaturesearch.network.CardPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
class CardRepository(
    val cardApiService: CardApiService
)   {

    fun getPagePhotos(): Flow<PagingData<Data>> {
        return Pager(
        config = PagingConfig(
        pageSize = 175,
        enablePlaceholders = true
        ),
        pagingSourceFactory =
        {
            CardPagingSource(cardApiService)
        }
        ).flow
    }
}