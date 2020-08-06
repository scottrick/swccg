package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard
import com.hatfat.swccg.data.SWCCGCardIdList
import javax.inject.Inject

class CardListViewModel @Inject constructor(
) : ViewModel() {

    private val cardIdListLiveData = MutableLiveData<SWCCGCardIdList>()
    private val navigateToSingleCardLiveData = MutableLiveData<SWCCGCard>()

    val navigateToSingleCard: LiveData<SWCCGCard>
        get() = navigateToSingleCardLiveData

    fun doneNavigating() {
        navigateToSingleCardLiveData.value = null
    }

    fun navigateTo(card: SWCCGCard) {
        navigateToSingleCardLiveData.value = card
    }

    val cardIdList: LiveData<SWCCGCardIdList>
        get() = cardIdListLiveData

    fun setCardIdList(cardIdList: SWCCGCardIdList) {
        cardIdListLiveData.value = cardIdList
    }
}
