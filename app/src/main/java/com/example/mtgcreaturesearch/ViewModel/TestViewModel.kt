package com.example.mtgcreaturesearch.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mtgcreaturesearch.Model.ShownCards
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: CardRepository,
): ViewModel() {

    fun getPaginationCards(): Flow<PagingData<ShownCards>> = repository.getCards().cachedIn(viewModelScope)
}