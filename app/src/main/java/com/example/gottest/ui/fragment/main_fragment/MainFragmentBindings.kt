package com.example.gottest.ui.fragment.main_fragment

import com.badoo.binder.using
import com.badoo.mvicore.android.AndroidBindings
import com.example.gottest.feature.LoadCharactersFeature
import com.example.gottest.ui.fragment.main_fragment.event.MainUiEventTransformer
import com.example.gottest.ui.fragment.main_fragment.viewmodel.MainViewModelTransformer

class MainFragmentBindings(
    view: MainFragment,
    private val feature: LoadCharactersFeature
) : AndroidBindings<MainFragment>(view) {

    override fun setup(view: MainFragment) {
        binder.bind(feature to view using MainViewModelTransformer())
        binder.bind(view to feature using MainUiEventTransformer())
    }

}