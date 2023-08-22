package edu.ucsc.cse118.assignment3.ui.message

import edu.ucsc.cse118.assignment3.data.Message

interface MessageListener {
    fun onClick(message: Message)
}