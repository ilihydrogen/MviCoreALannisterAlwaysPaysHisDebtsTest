package com.example.gottest.ui.fragment.main_fragment.event

import com.example.gottest.feature.LoadCharactersFeature
import com.example.gottest.ui.event.UIEvent

class MainUiEventTransformer : (UIEvent) -> LoadCharactersFeature.Wish? {

    override fun invoke(ev: UIEvent): LoadCharactersFeature.Wish? {
        return when (ev) {
            is UIEvent.UIReadyToLoad -> LoadCharactersFeature.Wish.LoadCharacters
            is UIEvent.RecyclerViewTrigger -> LoadCharactersFeature.Wish.LoadMoreCharacters(ev.page)
            is UIEvent.DetailsRequired -> null
        }
    }

}