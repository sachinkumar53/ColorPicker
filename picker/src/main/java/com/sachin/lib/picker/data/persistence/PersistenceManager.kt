package com.sachin.lib.picker.data.persistence

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sachin.lib.picker.toHex
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import android.graphics.Color as AndroidColor

object PersistenceManager {
    private const val TAG = "PersistenceHelper"
    private const val MAX_SAVE_COUNT = 8
    private const val PREF_NAME = "saved_colors"
    private val KEY_COLORS = stringPreferencesKey("colors")
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREF_NAME)


    suspend fun addColor(context: Context, color: Color) {
        val savedColors = getColorHexList(context).firstOrNull()
        savedColors?.toMutableList()?.apply {
            add(0, color.toHex(true))
        }?.take(MAX_SAVE_COUNT)?.also {
            saveColorList(context, it.toSet())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getColorList(context: Context): Flow<List<Color>> {
        return getColorHexList(context).mapLatest { colors ->
            colors.map { hex ->
                Color(AndroidColor.parseColor(hex))
            }
        }
    }

    private suspend fun saveColorList(context: Context, colors: Set<String>) {
        val colorStr = colors.joinToString(separator = ",")
        context.dataStore.edit { it[KEY_COLORS] = colorStr }
    }

    private fun getColorHexList(context: Context): Flow<List<String>> {
        return context.dataStore.data.map {
            it[KEY_COLORS]?.split(",") ?: emptyList()
        }
    }
}