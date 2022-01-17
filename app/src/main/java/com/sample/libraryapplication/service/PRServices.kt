package com.sample.libraryapplication.service

import com.sample.libraryapplication.dto.TownInfo
import com.sample.libraryapplication.dto.TownPicture
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PRServices {
    @GET("{districtName}/{townName}/index.json")
    //https://www.palestineremembered.com/json/Acre/al-Bassa/index.json
    fun getTownInfo(@Path("districtName") districtName: String, @Path("townName") townName: String): Call<TownInfo>
    @GET("{districtName}/{townName}/pictures.json")
    //https://www.palestineremembered.com/json/Acre/al-Bassa/pictures.json
    fun getTownPictures(@Path("districtName") districtName: String, @Path("townName") townName: String): Call<List<TownPicture>>
}