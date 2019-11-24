package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard
import javax.inject.Inject

class CardListViewModel @Inject constructor(
) : ViewModel() {

    private val cardListLiveData = MutableLiveData<List<SWCCGCard>>()
    private val navigateToSingleCardLiveData = MutableLiveData<SWCCGCard>()

    val navigateToSingleCard: LiveData<SWCCGCard>
        get() = navigateToSingleCardLiveData

    fun doneNavigating() {
        navigateToSingleCardLiveData.value = null
    }

    fun navigateTo(card: SWCCGCard) {
        navigateToSingleCardLiveData.value = card
    }

    val cardList: LiveData<List<SWCCGCard>>
        get() = cardListLiveData

    fun setCardList(cardList: List<SWCCGCard>) {
        cardListLiveData.value = cardList
    }
}
