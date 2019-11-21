package com.hatfat.swccg.app

import android.app.Application
import com.hatfat.swccg.fragments.MasterCardListFragment
import com.hatfat.swccg.fragments.SettingsFragment
import com.hatfat.swccg.fragments.SingleCardFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class
    ]
)
interface ApplicationComponent {
    fun inject(app: SWCCGApplication)
    fun inject(fragment: MasterCardListFragment)
    fun inject(fragment: SettingsFragment)
    fun inject(fragment: SingleCardFragment)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun swccgApplication(swccgApplication: SWCCGApplication): Builder
    }
}

class SWCCGApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        InjectionGraph.init(createComponent()).inject(this)
    }

    private fun createComponent(): ApplicationComponent {
        return DaggerApplicationComponent.builder().swccgApplication(this).build()
    }
}