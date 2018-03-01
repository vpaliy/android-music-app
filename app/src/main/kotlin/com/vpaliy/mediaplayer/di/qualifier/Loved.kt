package com.vpaliy.mediaplayer.di.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
@Target(allowedTargets = [AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER])
annotation class Loved