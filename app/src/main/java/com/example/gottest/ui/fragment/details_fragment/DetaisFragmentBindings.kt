package com.example.gottest.ui.fragment.details_fragment

import com.badoo.binder.using
import com.badoo.mvicore.android.AndroidBindings
import com.example.gottest.feature.CharacterDetailsFeature
import com.example.gottest.feature.LoadCharacterFeature
import com.example.gottest.ui.fragment.details_fragment.event.DetailsUiEventTransformer
import com.example.gottest.ui.fragment.details_fragment.event.DetailsUiEventTransformer2
import com.example.gottest.ui.fragment.details_fragment.viewmodel.DetailsViewModelTransformer
import com.example.gottest.util.combineLatest

class DetaisFragmentBindings(
    view: CharacterDetailsFragment,
    private val featureLoadCharacter: LoadCharacterFeature,
    private val featureCharacterDetails: CharacterDetailsFeature
) : AndroidBindings<CharacterDetailsFragment>(view) {

    override fun setup(view: CharacterDetailsFragment) {
        binder.bind(
            combineLatest(
                featureLoadCharacter,
                featureCharacterDetails
            ) to view using DetailsViewModelTransformer()
        )
        binder.bind(view to featureLoadCharacter using DetailsUiEventTransformer())
        binder.bind(view to featureCharacterDetails using DetailsUiEventTransformer2())
    }

}