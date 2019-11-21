package com.hatfat.swccg.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class SWCCGViewModelFactory @Inject constructor(
    private val masterCardListProvider: Provider<MasterCardListViewModel>
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == MasterCardListViewModel::class.java) {
            return masterCardListProvider.get() as T
        }

        throw RuntimeException("SWCCGViewModelFactory unable to create viewmodel for " + modelClass.simpleName + ".")
    }
}