package com.example.diploma.data.local.user

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.diploma.ui.purchaseList.PurchaseListProduct
import kotlinx.coroutines.flow.Flow

@Database(entities = [PurchaseListProduct::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun purchaseListProductDao(): PurchaseListProductDao
}

@Dao
interface PurchaseListProductDao {

    @Insert(onConflict = REPLACE)
    suspend fun save(purchaseListProduct: PurchaseListProduct)

    @Update
    suspend fun update(purchaseListProduct: PurchaseListProduct)

    @Query("SELECT * FROM PurchaseListProduct")
    fun load(): Flow<List<PurchaseListProduct>>

    @Query("SELECT * FROM PurchaseListProduct WHERE id=:id")
    suspend fun loadById(id: Int): PurchaseListProduct?

    @Delete
    suspend fun delete(purchaseListProduct: PurchaseListProduct)

    @Query("DELETE FROM PurchaseListProduct")
    suspend fun deleteAll()

}
