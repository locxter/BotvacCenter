package model

enum class EStatus(val displayName: String) {
    DISCONNECTED("Disconnected"),
    CONNECTED("Connected"),
    CLEANING_HOUSE("Cleaning house"),
    CLEANING_SPOT("Cleaning spot")
}