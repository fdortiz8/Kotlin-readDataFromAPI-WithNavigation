package edu.ucsc.cse118.assignment3.data

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Message (
    val id: String,
    @SerialName("member")
    val poster: String,
    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName("posted")
    val date: Instant,
    val content: String,
)