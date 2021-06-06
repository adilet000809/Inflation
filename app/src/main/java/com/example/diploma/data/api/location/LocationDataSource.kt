package com.example.diploma.data.api.location

import com.example.diploma.data.api.BaseDataSource
import com.example.diploma.data.model.Result
import com.example.diploma.ui.home.City
import com.example.diploma.ui.home.Supermarket
import retrofit2.Retrofit
import javax.inject.Inject

interface LocationDataSource {
    suspend fun fetchCity(): Result<List<City>>
    suspend fun fetchSupermarket(cityId: Int): Result<List<Supermarket>>
}

class LocationDataSourceImpl @Inject constructor(
    private val locationApiHelper: LocationApiHelper,
    private val retrofit: Retrofit
): LocationDataSource, BaseDataSource(retrofit) {

    override suspend fun fetchCity(): Result<List<City>> {
        return getResponse(
            request = { locationApiHelper.fetchCity() },
            defaultErrorMessage = "Error"
        )
    }

    override suspend fun fetchSupermarket(cityId: Int): Result<List<Supermarket>> {
        return getResponse(
            request = { locationApiHelper.fetchSupermarket(cityId) },
            defaultErrorMessage = "Error"
        )
    }


}