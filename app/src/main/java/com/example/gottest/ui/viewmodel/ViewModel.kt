package com.example.gottest.ui.viewmodel

import com.example.gottest.data.IceAndFireCharacter
import com.example.gottest.data.IceAndFireCharactersResponse

data class ViewModel(
    val isLoading: Boolean,
    val characters: IceAndFireCharactersResponse?,
    val character: IceAndFireCharacter?,
    val characterDetails: Pair<Int, String>? = null,
    val error: Throwable?,
    val append: Boolean = false
)