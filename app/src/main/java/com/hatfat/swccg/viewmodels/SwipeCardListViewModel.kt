package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCardIdList
import com.hatfat.swccg.repo.CardRepository
import javax.inject.Inject

class SwipeCardListViewModel @Inject constructor(
    val cardRepository: CardRepository
) : ViewModel()
{
    private val isFlippedLiveData = MutableLiveData<Boolean>()
    private val isRotatedLiveData = MutableLiveData<Boolean>()

    private val cardIdListLiveData = MutableLiveData<SWCCGCardIdList>()
    private val currentCardIndexLiveData = MutableLiveData<Int>()

    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    val cardIdList: LiveData<SWCCGCardIdList>
        get() = cardIdListLiveData

    val isFlippable: LiveData<Boolean> = Transformations.map(currentCardIndexLiveData) {
        cardRepository.cardsMap.value?.get(cardIdList.value?.cardIds?.get(it))?.isFlippable
    }

    fun setCardIdList(cardIdList: SWCCGCardIdList) {
        cardIdListLiveData.value = cardIdList
    }

    val currentCardIndex: LiveData<Int>
        get() = currentCardIndexLiveData

    fun setCurrentCardIndex(index: Int) {
        currentCardIndexLiveData.value = index
    }

    fun rotate() {
        isRotatedLiveData.value = !(isRotatedLiveData.value ?: false)
    }

    fun flip() {
        isFlippedLiveData.value = !(isFlippedLiveData.value ?: false)
    }
}
