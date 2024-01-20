package com.endeavorsheep.comiclibrary.model.api

import com.endeavorsheep.comiclibrary.model.CharactersApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {
    @GET
    fun getCharacters(@Query("nameStartsWith") name: String): Call<CharactersApiResponse>
}