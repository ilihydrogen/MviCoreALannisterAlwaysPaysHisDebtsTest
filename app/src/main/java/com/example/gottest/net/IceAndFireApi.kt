package com.example.gottest.net

import com.example.gottest.data.BaseAPIResponse
import com.example.gottest.data.IceAndFireCharacter
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface IceAndFireApi {

    @GET("api/characters")
    fun getCharacters(
        @Query("page") page: Int = 2
    ): Observable<List<IceAndFireCharacter>>

    @GET("api/characters/{id}")
    fun getCharacterById(
        @Path("id") id: Int
    ): Observable<IceAndFireCharacter>

    @GET
    fun getDetailsByUrl(@Url url: String): Observable<BaseAPIResponse>
}