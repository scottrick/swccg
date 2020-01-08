package com.hatfat.swccg.app

import android.app.Application
import com.hatfat.swccg.fragments.*
import com.hatfat.swccg.repo.CardRepository
import com.hatfat.swccg.repo.FormatRepository
import com.hatfat.swccg.repo.MetaDataRepository
import com.hatfat.swccg.repo.SetRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class
    ]
)
interface ApplicationComponent {
    fun inject(app: SWCCGApplication)
    fun inject(fragment: CardListFragment)
    fun inject(fragment: MasterCardListFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SingleCardFragment)
    fun inject(fragment: SwipeCardListFragment)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun swccgApplication(swccgApplication: SWCCGApplication): Builder
    }
}

class SWCCGApplication : Application() {

    /* Inject repositories here so they begin loading immediately on app startup */
    @Inject
    lateinit var metaDataRepository: MetaDataRepository

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var setRepository: SetRepository

    @Inject
    lateinit var formatRepository: FormatRepository

    override fun onCreate() {
        super.onCreate()
        InjectionGraph.init(createComponent()).inject(this)
    }

    private fun createComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().swccgApplication(this).build()
    }
}