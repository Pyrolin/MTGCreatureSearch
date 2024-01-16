//package com.example.mtgcreaturesearch.network
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.paging.PagingData
//import androidx.paging.cachedIn
//import com.example.mtgcreaturesearch.Model.Data
////import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.flow.Flow
//
//class PageViewModel(
//    private val repository: CardRepository
//): ViewModel() {
////    fun getPagePhotos(): Flow<PagingData<Data>> = repository.getPagePhotos().cachedIn(viewModelScope)
//
//    fun getPagePhotos(): Flow<PagingData<Data>>{
//        return repository.getPagePhotos()
//            .cachedIn(viewModelScope)
//    }
//}