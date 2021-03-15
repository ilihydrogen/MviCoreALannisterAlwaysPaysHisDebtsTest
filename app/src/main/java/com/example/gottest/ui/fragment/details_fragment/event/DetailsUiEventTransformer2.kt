package com.example.gottest.ui.fragment.details_fragment.event

import com.example.gottest.feature.CharacterDetailsFeature
import com.example.gottest.ui.event.UIEvent

class DetailsUiEventTransformer2 : (UIEvent) -> CharacterDetailsFeature.Wish? {

    override fun invoke(ev: UIEvent): CharacterDetailsFeature.Wish? {
        return when (ev) {
            is UIEvent.DetailsRequired -> CharacterDetailsFeature.Wish.LoadDetails(
                id = ev.resId,
                url = ev.url
            )
            else -> null
        }
    }

}