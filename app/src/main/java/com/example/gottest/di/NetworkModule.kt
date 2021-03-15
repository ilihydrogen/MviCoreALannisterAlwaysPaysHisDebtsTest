package com.example.gottest.di

import com.example.gottest.BuildConfig
import com.example.gottest.net.IceAndFireApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object NetworkModule {

    val networkModule = Kodein.Module {
        bind<Moshi>() with provider {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        bind<Retrofit>() with provider {
            Retrofit.Builder()
                .baseUrl(BuildConfig.ICE_AND_FIRE_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(instance()))
                .build()
        }

        bind<IceAndFireApi>() with provider {
            instance<Retrofit>().create(IceAndFireApi::class.java)
        }
    }

}