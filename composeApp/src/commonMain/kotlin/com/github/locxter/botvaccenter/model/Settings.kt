package com.github.locxter.botvaccenter.model

import java.io.Serializable

data class Settings(
    var robotName: String = "Robot",
    var address: String = "http://btvcbrdg.local",
    var username: String = "btvcbrdg",
    var password: String = "btvcbrdg"
) : Serializable
