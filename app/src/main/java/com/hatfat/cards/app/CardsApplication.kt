package com.hatfat.cards.app

import android.app.Application
import com.hatfat.cards.fragments.*
import com.hatfat.cards.repo.CardRepository
import com.hatfat.cards.repo.FormatRepository
import com.hatfat.cards.repo.MetaDataRepository
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
    fun inject(app: CardsApplication)
    fun inject(fragment: CardListFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SingleCardFragment)
    fun inject(fragment: SwipeCardListFragment)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun cardsApplication(cardsApplication: CardsApplication): Builder
    }
}

class CardsApplication : Application() {

    /* Inject repositories here so they begin loading immediately on app startup */
    @Inject
    lateinit var metaDataRepository: MetaDataRepository

    @Inject
    lateinit var cardRepository: CardRepository

    @Inject
    lateinit var formatRepository: FormatRepository

    override fun onCreate() {
        super.onCreate()
        InjectionGraph.init(createComponent()).inject(this)
    }

    private fun createComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().cardsApplication(this).build()
    }
}
