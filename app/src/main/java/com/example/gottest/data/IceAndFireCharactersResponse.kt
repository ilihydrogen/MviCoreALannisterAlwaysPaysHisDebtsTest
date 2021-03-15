package com.example.gottest.data

import android.os.Parcel
import android.os.Parcelable
import com.example.gottest.net.IceAndFireApiUtils

class IceAndFireCharactersResponse() : ArrayList<IceAndFireCharacter>(), Parcelable {

    val currentPage: Int get() = last().id / IceAndFireApiUtils.CHARACTERS_RESPONSE_PAGE_SIZE

    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IceAndFireCharactersResponse> {
        override fun createFromParcel(parcel: Parcel): IceAndFireCharactersResponse {
            return IceAndFireCharactersResponse(parcel)
        }

        override fun newArray(size: Int): Array<IceAndFireCharactersResponse?> {
            return arrayOfNulls(size)
        }
    }


}