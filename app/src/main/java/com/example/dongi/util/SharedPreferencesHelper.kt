package com.example.dongi.util

import android.content.Context

object SharedPreferencesHelper {

    private const val PREFERENCES_NAME = "MyAppPrefs"
    private const val AUTH_TOKEN_KEY = "AUTH_TOKEN"

    fun clearToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(AUTH_TOKEN_KEY)
        editor.apply()
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null)
    }
}
