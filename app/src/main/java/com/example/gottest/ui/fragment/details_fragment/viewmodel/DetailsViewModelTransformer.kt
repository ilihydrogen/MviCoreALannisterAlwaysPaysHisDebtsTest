package com.example.gottest.ui.fragment.details_fragment.viewmodel

import com.example.gottest.feature.CharacterDetailsFeature
import com.example.gottest.feature.LoadCharacterFeature
import com.example.gottest.ui.viewmodel.ViewModel

class DetailsViewModelTransformer :
        (Pair<LoadCharacterFeature.State, CharacterDetailsFeature.State>) -> ViewModel {

    override fun invoke(pair: Pair<LoadCharacterFeature.State, CharacterDetailsFeature.State>): ViewModel {
        val (state1, state2) = pair

        return ViewModel(
            isLoading = state1.isLoading,
            character = state1.iceAndFireCharacter,
            error = state2.error,
            characterDetails = state2.result,
            characters = null
        )
    }

}