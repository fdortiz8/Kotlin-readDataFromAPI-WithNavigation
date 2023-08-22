package edu.ucsc.cse118.assignment3.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.ucsc.cse118.assignment3.R
import edu.ucsc.cse118.assignment3.data.Message
import edu.ucsc.cse118.assignment3.databinding.FragmentMessageBinding
import edu.ucsc.cse118.assignment3.model.SharedViewModel
import edu.ucsc.cse118.assignment3.model.ViewModelEvent

class MessageFragment : Fragment(), MessageListener {

    private lateinit var binding: FragmentMessageBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    private val errorObserver = Observer<ViewModelEvent<String>> { event ->
        val error = event.getUnhandledContent()
        if (error != null) {
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
        }
    }

    private val messageObserver = Observer<ArrayList<Message>> { messages ->
        recyclerView.adapter = MessageAdapter(messages, this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.error.observe(this, errorObserver)
        sharedViewModel.messages.observe(this, messageObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.error.removeObserver(errorObserver)
        sharedViewModel.messages.removeObserver(messageObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = FragmentMessageBinding.inflate(inflater, container, false)

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MessageAdapter(arrayListOf(), this)
        sharedViewModel.getMessages()

        val fab: FloatingActionButton = view.findViewById(R.id.fab)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_messageFragment_to_newMessageFragment)
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = sharedViewModel.channel.value?.getRawContent()?.name
    }

    override fun onClick(message: Message) {
        sharedViewModel.setMessage(message)
        findNavController().navigate(R.id.action_messageFragment_to_messageDetailFragment)
    }
}