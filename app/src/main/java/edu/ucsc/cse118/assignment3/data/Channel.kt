package edu.ucsc.cse118.assignment3.data

import kotlinx.serialization.Serializable

@Serializable
class Channel (
    val id: String,
    val name: String,
    val messages: Int?
    )