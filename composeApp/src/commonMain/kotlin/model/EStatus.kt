package model

enum class EStatus(val displayName: String) {
    STATUS_DISCONNECTED("Disconnected"),
    STATUS_CONNECTED("Connected"),
    STATUS_ERROR("Error")
}