package com.example.gottest

import android.app.Application
import com.example.gottest.di.FeaturesModule
import com.example.gottest.di.NetworkModule
import org.kodein.di.Kodein

class GOTTestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        GOTTestApp.app = this

        kodein = Kodein {
            import(FeaturesModule.featuresModule)
            import(NetworkModule.networkModule)
        }
    }

    companion object {
        lateinit var app: GOTTestApp
        lateinit var kodein: Kodein
    }

}