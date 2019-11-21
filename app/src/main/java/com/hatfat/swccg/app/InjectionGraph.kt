package com.hatfat.swccg.app

object InjectionGraph {
    lateinit var component: ApplicationComponent

    @JvmStatic
    fun init(component: ApplicationComponent): ApplicationComponent {
        InjectionGraph.component = component
        return InjectionGraph.component
    }
}