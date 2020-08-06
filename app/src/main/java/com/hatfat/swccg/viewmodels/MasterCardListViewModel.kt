package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.repo.CardRepository
import javax.inject.Inject

class MasterCardListViewModel @Inject constructor(
    val cardRepository: CardRepository
) : ViewModel() {

    private val navigateToSingleCardLiveData = MutableLiveData<SWCCGCard>()

    val navigateToSingleCard: LiveData<SWCCGCard>
        get() = navigateToSingleCardLiveData

    fun doneNavigating() {
        navigateToSingleCardLiveData.value = null
    }

    fun navigateTo(card: SWCCGCard) {
        navigateToSingleCardLiveData.value = card
    }

    val cardIds: LiveData<SWCCGCardIdList>
        get() = cardRepository.sortedCardIds
}
