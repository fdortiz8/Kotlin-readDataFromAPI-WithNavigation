package edu.ucsc.cse118.assignment3.ui.messageDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import edu.ucsc.cse118.assignment3.data.Message
import edu.ucsc.cse118.assignment3.databinding.FragmentMessageDetailBinding
import edu.ucsc.cse118.assignment3.model.SharedViewModel
import edu.ucsc.cse118.assignment3.model.ViewModelEvent
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MessageDetailFragment: Fragment() {
    private lateinit var binding : FragmentMessageDetailBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val errorObserver = Observer<ViewModelEvent<String>> { event ->
        var error = event.getUnhandledContent()
        if (error != null) {
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
        }
    }

    private val messageDetailObserver = Observer<ViewModelEvent<Message>> { event ->
        val messageDetail = event.getUnhandledContent()
        if (messageDetail != null) {
            (activity as AppCompatActivity?)!!.supportActionBar!!.title = messageDetail.poster
            //binding.poster.text = messageDetail.poster
            val instant = messageDetail.date.toJavaInstant()
            val z = ZoneId.of("+00:00")
            val zdt = instant.atZone(z)
            val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm:ss a")
            val newFormattedDate = formatter.format(zdt)
            binding.date.text = newFormattedDate
            binding.content.text = messageDetail.content
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.error.observe(this, errorObserver)
        sharedViewModel.messageDetail.observe(this, messageDetailObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.error.removeObserver(errorObserver)
        sharedViewModel.messageDetail.removeObserver(messageDetailObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentMessageDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getMessageDetail()
    }

}