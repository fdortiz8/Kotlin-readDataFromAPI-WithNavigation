package edu.ucsc.cse118.assignment3.repo

import edu.ucsc.cse118.assignment3.data.Member
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MemberRepository {

    fun login(email: String, password: String) : Member {
        with(URL(url).openConnection() as HttpsURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            outputStream.write(Json.encodeToString(LoginCredentials(email, password)).toByteArray())
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return Json.decodeFromString(inputStream.bufferedReader().use { it.readText() })
            }
            throw Exception("Failed to login : HTTP $responseCode")
        }
    }

    @Serializable
    data class LoginCredentials(val email: String, val password: String)

    companion object {
        private const val url = "https://cse118.com/api/v2/login"
    }
}