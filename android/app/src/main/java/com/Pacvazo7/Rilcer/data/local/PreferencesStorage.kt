package com.Pacvazo7.Rilcer.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun readIntSet(key: String): Set<Int> {
        val raw = prefs.getString(key, "") ?: ""
        if (raw.isEmpty()) return emptySet()
        return raw.split(SEPARATOR).mapNotNull { it.toIntOrNull() }.toSet()
    }

    fun writeIntSet(key: String, value: Set<Int>) {
        prefs.edit().putString(key, value.sorted().joinToString(SEPARATOR)).apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val STORE_NAME = "billion_vault_quest"
        private const val SEPARATOR = ","

        const val KEY_CRYSTALS = "crystals"
        const val KEY_DEPTH_RECORD = "depth_record"
        const val KEY_EXPEDITIONS = "expeditions"
        const val KEY_OBJECTIVE_INDEX = "objective_index"
        const val KEY_CLEARED_BEST = "cleared_best"
        const val KEY_ARTIFACTS = "artifacts"
        const val KEY_TOOL_PREFIX = "tool_level_"
        const val KEY_COLS = "vault_cols"
        const val KEY_PRECISE = "clue_precise"
        const val KEY_SUPPLIES = "starting_supplies"
        const val KEY_REDUCED_MOTION = "reduced_motion"
    }
}
