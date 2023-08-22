package edu.ucsc.cse118.assignment3.repo

import androidx.lifecycle.LiveData
import edu.ucsc.cse118.assignment3.data.Channel
import edu.ucsc.cse118.assignment3.data.Member
import edu.ucsc.cse118.assignment3.data.Message
import edu.ucsc.cse118.assignment3.model.ViewModelEvent
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MessageRepository {

    fun getAllMessages(member: Member?, channel: ViewModelEvent<Channel>?) : ArrayList<Message> {
        val channelId = channel?.getRawContent()?.id
        val path = "$url/$channelId/message"
        with(URL(path).openConnection() as HttpsURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "text/html; charset=UTF-8n")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer ${member?.accessToken}")
            val response = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return Json.decodeFromString(response.toString())
            }
            throw Exception("Failed to GET HTTP $responseCode")
        }
    }

    fun getOneMessage(member: Member?, message: Message?) : Message {
        //val channelId = channel?.getRawContent()?.id
        val path = "$url/message/${message?.id}"
        with(URL(path).openConnection() as HttpsURLConnection) {
            requestMethod = "GET"
            setRequestProperty("Content-Type", "text/html; charset=UTF-8n")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer ${member?.accessToken}")
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                return Json.decodeFromString(inputStream.bufferedReader().use { it.readText() })
            }
            throw Exception("Failed to GET HTTP $responseCode")
        }
    }

    fun addOneMessage(member: Member?, channel: LiveData<ViewModelEvent<Channel>>, message: Message) : Message {
        val channelId = channel.value?.getUnhandledContent()?.id
        //message.id = UUID.randomUUID().toString()
        val path = "$url/${channel.value?.getRawContent()?.id}/message"
        with(URL(path).openConnection() as HttpsURLConnection) {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            setRequestProperty("Authorization", "Bearer ${member?.accessToken}")
            outputStream.write(Json.encodeToString(message).toByteArray())
            if (responseCode == HttpsURLConnection.HTTP_CREATED) {
                return Json.decodeFromString(inputStream.bufferedReader().use { it.readText() })
            }
            if (responseCode == HttpsURLConnection.HTTP_CONFLICT) {
                throw Exception("Message with code ${message.id} exists!")
            }
            throw Exception("Failed to PUT HTTP $responseCode")
        }
    }


    companion object {
        private const val url = "https://cse118.com/api/v2/channel"
    }
}



