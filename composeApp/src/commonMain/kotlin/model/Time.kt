package model

data class Time(val hour: Int, var minute: Int) {
    fun getFormated(): String {
        return "${if (hour < 10) "0$hour" else hour}:${if (minute < 10) "0$minute" else minute}"
    }
}
