package com.edwinderepuesto.jpclient.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("username")
    val username: String,

    @SerialName("email")
    val email: String,
)