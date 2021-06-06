package com.example.diploma.data.api.location

import com.example.diploma.ui.home.City
import com.example.diploma.ui.home.Supermarket
import retrofit2.Response
import javax.inject.Inject

interface LocationApiHelper {
    suspend fun fetchCity(): Response<List<City>>
    suspend fun fetchSupermarket(cityId: Int): Response<List<Supermarket>>
}

class LocationApiHelperImpl @Inject constructor(
    private val locationService: LocationService
): LocationApiHelper {

    override suspend fun fetchCity(): Response<List<City>> {
        return locationService.fetchCity()
    }

    override suspend fun fetchSupermarket(cityId: Int): Response<List<Supermarket>> {
        return locationService.fetchSupermarket(cityId)
    }

}