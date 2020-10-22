package com.hatfat.cards.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class SWCCGViewModelFactory @Inject constructor(
    private val cardListProvider: Provider<CardListViewModel>,
    private val searchProvider: Provider<SearchViewModel>,
    private val swipeCardListProvider: Provider<SwipeCardListViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == SearchViewModel::class.java) {
            return searchProvider.get() as T
        }
        if (modelClass == CardListViewModel::class.java) {
            return cardListProvider.get() as T
        }
        if (modelClass == SwipeCardListViewModel::class.java) {
            return swipeCardListProvider.get() as T
        }

        throw RuntimeException("SWCCGViewModelFactory unable to create viewmodel for " + modelClass.simpleName + ".")
    }
}
