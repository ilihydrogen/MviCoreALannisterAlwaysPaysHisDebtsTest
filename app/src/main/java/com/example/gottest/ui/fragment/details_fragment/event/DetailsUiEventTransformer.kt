package com.example.gottest.ui.fragment.details_fragment.event

import com.example.gottest.feature.LoadCharacterFeature
import com.example.gottest.ui.event.UIEvent

class DetailsUiEventTransformer : (UIEvent) -> LoadCharacterFeature.Wish? {

    override fun invoke(ev: UIEvent): LoadCharacterFeature.Wish? {
        return when (ev) {
            is UIEvent.UIReadyToLoad -> LoadCharacterFeature.Wish.LoadCharacter(id = ev.value)
            else -> null
        }
    }

}