package com.hatfat.swccg.app

import android.content.res.Resources
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule  {

    @Provides
    fun provideResources(application: SWCCGApplication): Resources {
        return application.resources
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}