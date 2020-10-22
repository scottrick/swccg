package com.hatfat.cards.viewmodels

import androidx.lifecycle.*
import com.hatfat.cards.data.SWCCGCard

class SingleCardViewModel : ViewModel() {

    private val cardLiveData = MutableLiveData<SWCCGCard>()
    private val isFlippedLiveData = MutableLiveData<Boolean>()
    private val isRotatedLiveData = MutableLiveData<Boolean>()

    init {
        isFlippedLiveData.value = false
        isRotatedLiveData.value = false
    }

    val cardUrl = MediatorLiveData<String>().apply {
        this.addSource(cardLiveData) { updateCardUrl() }
        this.addSource(isFlippedLiveData) { updateCardUrl() }
    }

    val isFlippable: LiveData<Boolean> = Transformations.map(cardLiveData) {
        it.isFlippable
    }

    val isRotated: LiveData<Boolean>
        get() = isRotatedLiveData

    private fun updateCardUrl() {
        isFlippedLiveData.value?.let { flipped ->
            cardLiveData.value?.let { card ->
                if (card.isFlippable && flipped) {
                    cardUrl.value = card.back?.imageUrl
                }
                else {
                    cardUrl.value = card.front.imageUrl
                }
            }
        }
    }

    fun setCard(card: SWCCGCard) {
        cardLiveData.value = card
    }

    fun rotate() {
        isRotatedLiveData.value = !(isRotatedLiveData.value ?: false)
    }

    fun flip() {
        isFlippedLiveData.value = !(isFlippedLiveData.value ?: false)
    }
}
