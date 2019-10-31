package ru.lorens.peaceofmind.helpers

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class SharedPreference(val context: Context) {
    private val PREFS_NAME = "prefs"
    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME: String, date: Date) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putLong(KEY_NAME, date.time)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueLong(KEY_NAME: String): Long {
        return sharedPref.getLong(KEY_NAME, 0)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        //sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        editor.clear()
        editor.apply()
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }
}