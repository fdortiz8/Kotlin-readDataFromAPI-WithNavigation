package edu.ucsc.cse118.assignment3.ui.channel

import edu.ucsc.cse118.assignment3.data.Channel

interface ChannelListener {
    fun onClick(channel: Channel)
}