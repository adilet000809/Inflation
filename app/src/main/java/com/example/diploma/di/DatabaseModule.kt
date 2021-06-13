package com.example.diploma.di

import android.content.Context
import androidx.room.Room
import com.example.diploma.data.local.user.AppDatabase
import com.example.diploma.data.local.user.PurchaseListProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePurchaseListProductDao(appDatabase: AppDatabase): PurchaseListProductDao {
        return appDatabase.purchaseListProductDao()
    }

}