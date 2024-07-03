package lib


class SettingsController {
    private val settingsBackend: com.russhwolf.settings.Settings = com.russhwolf.settings.Settings()
    fun readSettings(): model.Settings {
        val robotName = settingsBackend.getString("robotName", model.Settings().robotName)
        val address = settingsBackend.getString("address", model.Settings().address)
        val username = settingsBackend.getString("username", model.Settings().username)
        val password = settingsBackend.getString("password", model.Settings().password)
        return model.Settings(
            robotName, address, username, password
        )
    }

    fun writeSettings(settings: model.Settings) {
        settingsBackend.putString("robotName", settings.robotName)
        settingsBackend.putString("address", settings.address)
        settingsBackend.putString("username", settings.username)
        settingsBackend.putString("password", settings.password)
    }

    fun clearSettings() {
        settingsBackend.clear()
    }
}