package com.example.gottest.di

import com.example.gottest.feature.CharacterDetailsFeature
import com.example.gottest.feature.LoadCharacterFeature
import com.example.gottest.feature.LoadCharactersFeature
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

object FeaturesModule {

    val featuresModule = Kodein.Module {
        bind<LoadCharactersFeature>() with provider { LoadCharactersFeature() }
        bind<LoadCharacterFeature>() with provider { LoadCharacterFeature() }
        bind<CharacterDetailsFeature>() with provider { CharacterDetailsFeature() }
    }

}