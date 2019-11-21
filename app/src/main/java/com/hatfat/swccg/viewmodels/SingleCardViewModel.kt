package com.hatfat.swccg.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hatfat.swccg.data.SWCCGCard

class SingleCardViewModel : ViewModel() {

    private val cardCodeLiveData = MutableLiveData<SWCCGCard>()

    val cardCode: LiveData<SWCCGCard>
        get() = cardCodeLiveData

    fun setCard(card: SWCCGCard) {
        cardCodeLiveData.value = card
    }
}