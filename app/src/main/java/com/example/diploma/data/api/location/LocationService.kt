package com.example.diploma.data.api.location

import com.example.diploma.ui.home.City
import com.example.diploma.ui.home.Supermarket
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationService {

    @GET("cities")
    suspend fun fetchCity(): Response<List<City>>

    @GET("supermarkets/")
    suspend fun fetchSupermarket(@Query("cityId") cityId: Int): Response<List<Supermarket>>

}