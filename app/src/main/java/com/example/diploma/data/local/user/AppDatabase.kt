package com.example.diploma.data.local.user

//import androidx.room.*
//import androidx.room.OnConflictStrategy.REPLACE
//import com.example.diploma.ui.login.ui.login.User
//import kotlinx.coroutines.flow.Flow
//
//@Database(entities = [User::class], version = 1)
//abstract class AppDatabase: RoomDatabase() {
//    abstract fun userDao(): UserDao
//}
//
//@Dao
//interface UserDao {
//    @Insert(onConflict = REPLACE)
//    fun save(user: User)
//
//    @Query("SELECT * FROM User LIMIT 1")
//    fun load(): User
//
//    @Query("SELECT COUNT(*) FROM user WHERE lastUpdate >= :freshTimeout")
//    fun hasUser(freshTimeout: Long): Int
//}
