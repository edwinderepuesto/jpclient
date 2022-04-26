package com.edwinderepuesto.jpclient.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Post(
    @PrimaryKey
    @SerialName("id")
    val id: Int,

    @SerialName("userId")
    val userId: Int,

    @SerialName("title")
    val title: String,

    @SerialName("body")
    val body: String,
) {
    var isFavorite: Boolean = false
}