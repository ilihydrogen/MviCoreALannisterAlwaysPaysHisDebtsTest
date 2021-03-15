package com.example.gottest.ui.event

import com.example.gottest.net.IceAndFireApiUtils

sealed class UIEvent {
    class RecyclerViewTrigger(val page: Int = IceAndFireApiUtils.CHARACTERS_START_PAGE) : UIEvent()
    class UIReadyToLoad(val value: Int = IceAndFireApiUtils.CHARACTERS_RESPONSE_PAGE_SIZE) :
        UIEvent()

    class DetailsRequired(val resId: Int, val url: String) : UIEvent()
}