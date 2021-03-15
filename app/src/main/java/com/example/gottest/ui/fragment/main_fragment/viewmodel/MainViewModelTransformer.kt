package com.example.gottest.ui.fragment.main_fragment.viewmodel

import com.example.gottest.feature.LoadCharactersFeature
import com.example.gottest.ui.viewmodel.ViewModel

class MainViewModelTransformer : (LoadCharactersFeature.State) -> ViewModel {

    override fun invoke(state: LoadCharactersFeature.State): ViewModel {
        return ViewModel(
            isLoading = state.isLoading,
            characters = state.iceAndFireCharacters,
            character = null,
            error = state.error,
            append = state.append
        )
    }

}