package com.github.locxter.botvaccenter.model

data class Schedule(
    var monday: Time? = null,
    var tuesday: Time? = null,
    var wednesday: Time? = null,
    var thursday: Time? = null,
    var friday: Time? = null,
    var saturday: Time? = null,
    var sunday: Time? = null,
    var isEnabled: Boolean = true
)
