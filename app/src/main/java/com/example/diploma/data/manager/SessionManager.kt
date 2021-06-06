package com.example.diploma.data.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.diploma.R
import com.example.diploma.ui.home.City

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val CURRENT_CITY_ID = "current_city_id"
        const val CURRENT_CITY_NAME = "current_city_NAME"
        const val CURRENT_SUPERMARKET_ID = "current_supermarket_id"
        const val CURRENT_SUPERMARKET_NAME = "current_supermarket_NAME"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, "Bearer $token")
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun deleteAuthToken() {
        val editor = prefs.edit()
        editor.clear()
    }

    fun saveCurrentCityId(cityId: Int) {
        val editor = prefs.edit()
        editor.putInt(CURRENT_CITY_ID, cityId)
        editor.apply()
    }

    fun fetchCurrentCityId(): Int {
        return prefs.getInt(CURRENT_CITY_ID, -1)
    }

    fun saveCurrentCityName(cityName: String) {
        val editor = prefs.edit()
        editor.putString(CURRENT_CITY_NAME,  cityName)
        editor.apply()
    }

    fun fetchCurrentCityName(): String? {
        return prefs.getString(CURRENT_CITY_NAME, null)
    }

    fun saveCurrentSupermarketId(supermarketId: Int) {
        val editor = prefs.edit()
        editor.putInt(CURRENT_SUPERMARKET_ID, supermarketId)
        editor.apply()
    }

    fun fetchCurrentSupermarketId(): Int {
        return prefs.getInt(CURRENT_SUPERMARKET_ID, -1)
    }

    fun saveCurrentSupermarketName(supermarketName: String) {
        val editor = prefs.edit()
        editor.putString(CURRENT_SUPERMARKET_NAME,  supermarketName)
        editor.apply()
    }

    fun fetchCurrentSupermarketName(): String? {
        return prefs.getString(CURRENT_SUPERMARKET_NAME, null)
    }

}