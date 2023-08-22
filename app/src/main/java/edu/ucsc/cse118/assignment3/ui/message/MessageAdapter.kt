package edu.ucsc.cse118.assignment3.ui.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.ucsc.cse118.assignment3.R
import edu.ucsc.cse118.assignment3.data.Message
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageAdapter(private val messages: ArrayList<Message>, private val listener: MessageListener) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
        holder.itemView.setOnClickListener { listener.onClick(messages[position]) }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val poster: TextView = itemView.findViewById(R.id.poster)
        private val content: TextView = itemView.findViewById(R.id.content)
        private val date: TextView = itemView.findViewById(R.id.date)

        fun bind (message: Message) {
            poster.text = message.poster
            val instant = message.date.toJavaInstant()
            val z = ZoneId.of("+00:00")
            val zdt = instant.atZone(z)
            val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy, hh:mm:ss a")
            val newFormattedDate = formatter.format(zdt)
            date.text = newFormattedDate
            content.text = message.content
        }
    }
}