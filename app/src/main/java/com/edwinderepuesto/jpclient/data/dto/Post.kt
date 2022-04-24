package com.edwinderepuesto.jpclient.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerialName("id")
    val id: String,

    @SerialName("userId")
    val userId: String,

    @SerialName("title")
    val title: String,

    @SerialName("body")
    val body: String,
)