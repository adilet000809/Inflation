package com.example.diploma.di

import android.content.Context
import com.example.diploma.data.api.auth.AuthApiHelper
import com.example.diploma.data.api.auth.AuthApiHelperImpl
import com.example.diploma.data.api.auth.AuthService
import com.example.diploma.data.api.auth.AuthDataSource
import com.example.diploma.data.api.auth.AuthDataSourceImpl
import com.example.diploma.data.api.history.*
import com.example.diploma.data.api.location.*
import com.example.diploma.data.api.product.*
import com.example.diploma.data.api.profile.*
import com.example.diploma.data.manager.SessionManager
import com.example.diploma.data.util.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Singleton
    @Provides
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor { chain ->
                    val newRequest = chain.request().newBuilder()
                            //.addHeader("Authorization", "Bearer ${sessionManager.fetchAuthToken()}")
                            .build()
                    chain.proceed(newRequest)
                }
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)

    @Singleton
    @Provides
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideAuthApiHelper(authService: AuthService): AuthApiHelper = AuthApiHelperImpl(authService)


    @Singleton
    @Provides
    fun provideLoginDataSource(
            authApiHelper: AuthApiHelper,
            retrofit: Retrofit
    ): AuthDataSource = AuthDataSourceImpl(authApiHelper, retrofit)

    @Singleton
    @Provides
    fun provideProfileService(retrofit: Retrofit): ProfileService = retrofit.create(ProfileService::class.java)

    @Singleton
    @Provides
    fun provideProfileApiHelper(profileService: ProfileService): ProfileApiHelper = ProfileApiHelperImpl(profileService)


    @Singleton
    @Provides
    fun provideProfileDataSource(
        profileApiHelper: ProfileApiHelper,
        retrofit: Retrofit
    ): ProfileDataSource = ProfileDataSourceImpl(profileApiHelper, retrofit)

    @Singleton
    @Provides
    fun provideLocationService(retrofit: Retrofit): LocationService = retrofit.create(LocationService::class.java)

    @Singleton
    @Provides
    fun provideLocationApiHelper(locationService: LocationService): LocationApiHelper = LocationApiHelperImpl(locationService)


    @Singleton
    @Provides
    fun provideLocationDataSource(
        locationApiHelper: LocationApiHelper,
        retrofit: Retrofit
    ): LocationDataSource = LocationDataSourceImpl(locationApiHelper, retrofit)


    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit): ProductService = retrofit.create(ProductService::class.java)

    @Singleton
    @Provides
    fun provideProductApiHelper(productService: ProductService): ProductApiHelper = ProductApiHelperImpl(productService)

    @Singleton
    @Provides
    fun provideProductDataSource(
        productApiHelper: ProductApiHelper,
        retrofit: Retrofit
    ): ProductDataSource = ProductDataSourceImpl(productApiHelper, retrofit)

    @Singleton
    @Provides
    fun provideHistoryService(retrofit: Retrofit): HistoryService = retrofit.create(HistoryService::class.java)

    @Singleton
    @Provides
    fun provideHistoryApiHelper(historyService: HistoryService): HistoryApiHelper = HistoryApiHelperImpl(historyService)

    @Singleton
    @Provides
    fun provideHistoryDataSource(
        historyApiHelper: HistoryApiHelper,
        retrofit: Retrofit
    ): HistoryDataSource = HistoryDataSourceImpl(historyApiHelper, retrofit)

}