package com.example.gottest.data

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json


//"father": "",
//"mother": "",
//"spouse": "",
//"allegiances": [
//"https://anapioficeandfire.com/api/houses/362"
//],
//"books": [
//"https://anapioficeandfire.com/api/books/5"
//],
//"povBooks": [
//"https://anapioficeandfire.com/api/books/1",
//"https://anapioficeandfire.com/api/books/2",
//"https://anapioficeandfire.com/api/books/3",
//"https://anapioficeandfire.com/api/books/8"
//],
//"tvSeries": [
//"Season 1",
//"Season 2",
//"Season 3",
//"Season 4",
//"Season 5",
//"Season 6"
//],
//"playedBy": [
//"Kit Harington"
//]

data class IceAndFireCharacter(
    @field:Json(name = "url") val url: String,
    @field:Json(name = "gender") val gender: String,
    @field:Json(name = "culture") val culture: String,
    @field:Json(name = "died") val died: String,
    @field:Json(name = "titles") val titles: List<String>,
    @field:Json(name = "aliases") val aliases: List<String>,
    @field:Json(name = "father") val father: String,
    @field:Json(name = "mother") val mother: String,
    @field:Json(name = "spouse") val spouse: String,
    @field:Json(name = "allegiances") val allegiances: List<String>,
    @field:Json(name = "books") val books: List<String>,
    @field:Json(name = "povBooks") val povBooks: List<String>,
    @field:Json(name = "tvSeries") val tvSeries: List<String>,
    @field:Json(name = "playedBy") val playedBy: List<String>,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "born") val born: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.createStringArrayList() ?: listOf(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(gender)
        parcel.writeString(culture)
        parcel.writeString(died)
        parcel.writeStringList(titles)
        parcel.writeStringList(aliases)
        parcel.writeString(father)
        parcel.writeString(mother)
        parcel.writeString(spouse)
        parcel.writeStringList(allegiances)
        parcel.writeStringList(books)
        parcel.writeStringList(povBooks)
        parcel.writeStringList(tvSeries)
        parcel.writeStringList(playedBy)
        parcel.writeString(name)
        parcel.writeString(born)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IceAndFireCharacter> {
        override fun createFromParcel(parcel: Parcel): IceAndFireCharacter {
            return IceAndFireCharacter(parcel)
        }

        override fun newArray(size: Int): Array<IceAndFireCharacter?> {
            return arrayOfNulls(size)
        }
    }

    val id: Int get() = url.split("/").last().toInt()
}