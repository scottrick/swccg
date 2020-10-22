package com.hatfat.cards.app

import android.content.res.Resources
import com.google.gson.Gson
import com.hatfat.cards.data.CardsConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideResources(application: CardsApplication): Resources {
        return application.resources
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesConfig(): CardsConfig {
        return CardsConfig(
            shouldUsePlayStoreImages = false
        )
    }
}
