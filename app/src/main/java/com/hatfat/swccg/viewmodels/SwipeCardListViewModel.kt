package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard

class SwipeCardListViewModel : ViewModel()
{
    private val isFlippedLiveData = MutableLiveData<Boolean>()
    private val isRotatedLiveData = MutableLiveData<Boolean>()

    private val cardListLiveData = MutableLiveData<List<SWCCGCard>>()
    private val currentCardIndexLiveData = MutableLiveData<Int>()

    val isFlipped: LiveData<Boolean>
        get() = isFlippedLiveData

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    val cardList: LiveData<List<SWCCGCard>>
        get() = cardListLiveData

    val isFlippable: LiveData<Boolean> = Transformations.map(currentCardIndexLiveData) {
        cardList.value?.get(it)?.hasImageUrl2
    }

    fun setCardList(cardList: List<SWCCGCard>) {
        cardListLiveData.value = cardList
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