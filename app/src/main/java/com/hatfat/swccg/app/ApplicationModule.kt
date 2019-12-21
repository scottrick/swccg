package com.hatfat.swccg.app

import android.content.res.Resources
import com.google.gson.Gson
import com.hatfat.swccg.data.SWCCGConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideResources(application: SWCCGApplication): Resources {
        return application.resources
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providesConfig(): SWCCGConfig {
        return SWCCGConfig(
            shouldUsePlaystoreImages = false
        )
    }
}