package edu.ucsc.cse118.assignment3.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Workspace (
    val name: String,
    val id: String,
    val owner: String,
    val channels: Int
    )