package com.github.locxter.botvaccenter.lib

import com.github.locxter.botvaccenter.model.Settings


class SettingsController {
    private val settingsBackend: com.russhwolf.settings.Settings = com.russhwolf.settings.Settings()
    fun readSettings(): Settings {
        val robotName = settingsBackend.getString("robotName", Settings().robotName)
        val address = settingsBackend.getString("address", Settings().address)
        val username = settingsBackend.getString("username", Settings().username)
        val password = settingsBackend.getString("password", Settings().password)
        return Settings(
            robotName, address, username, password
        )
    }

    fun writeSettings(settings: Settings) {
        settingsBackend.putString("robotName", settings.robotName)
        settingsBackend.putString("address", settings.address)
        settingsBackend.putString("username", settings.username)
        settingsBackend.putString("password", settings.password)
    }

    fun clearSettings() {
        settingsBackend.clear()
    }
}