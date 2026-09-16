package com.kafein.tasktracker.data.local

import android.content.Context

class InitialDataPreferences(
    context: Context
) {

    private val preferences = context.getSharedPreferences(
        "task_tracker_preferences",
        Context.MODE_PRIVATE
    )

    fun isInitialDataLoaded(): Boolean {
        return preferences.getBoolean(
            KEY_INITIAL_DATA_LOADED,
            false
        )
    }

    fun setInitialDataLoaded() {
        preferences.edit()
            .putBoolean(KEY_INITIAL_DATA_LOADED, true)
            .apply()
    }

    companion object {
        private const val KEY_INITIAL_DATA_LOADED =
            "initial_data_loaded"
    }
}