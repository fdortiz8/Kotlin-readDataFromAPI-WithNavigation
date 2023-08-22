package edu.ucsc.cse118.assignment3.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Member (
    val name: String,
    @SerialName("id")
    val email: String,
    val role: String,
    val accessToken: String,
)