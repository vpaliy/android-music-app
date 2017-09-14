package com.vpaliy.mediaplayer.di.component

import com.vpaliy.mediaplayer.di.module.InteractorModule
import dagger.Component

@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(InteractorModule::class))
interface ViewComponent{

}