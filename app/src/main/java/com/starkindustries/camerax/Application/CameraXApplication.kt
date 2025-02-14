package com.starkindustries.camerax.Application

import android.app.Application
import com.starkindustries.camerax.Component.DaggerDependencyInjectionComponents
import com.starkindustries.camerax.Component.DependencyInjectionComponents
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CameraXApplication:Application() {

    lateinit var dependencyInjectionComponents: DependencyInjectionComponents

    override fun onCreate() {
        super.onCreate()
        dependencyInjectionComponents = DaggerDependencyInjectionComponents.builder().build()
    }
}