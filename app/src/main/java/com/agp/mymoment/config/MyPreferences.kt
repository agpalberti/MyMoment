package com.agp.mymoment.config

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.prefs.Preferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MyPreferences @Inject constructor(private val context: Context){

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userToken")
        private val THEME_SETTINGS = stringPreferencesKey("theme_setting")
    }

    val accessTheme: Flow<String> = context.dataStore.data.map { it[THEME_SETTINGS]?:"" }

    suspend fun saveThemeSetting(darkMode: Boolean){
        context.dataStore.edit{
            it[THEME_SETTINGS] = darkMode.toString()
        }
    }
}