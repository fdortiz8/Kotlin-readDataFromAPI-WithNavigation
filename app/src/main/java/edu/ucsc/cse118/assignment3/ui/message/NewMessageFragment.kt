package edu.ucsc.cse118.assignment3.ui.message

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import edu.ucsc.cse118.assignment3.data.Message
import edu.ucsc.cse118.assignment3.databinding.FragmentNewMessageBinding
import edu.ucsc.cse118.assignment3.model.SharedViewModel
import edu.ucsc.cse118.assignment3.model.ViewModelEvent
import kotlinx.datetime.Clock
import java.util.UUID


class NewMessageFragment : Fragment() {
    private lateinit var binding: FragmentNewMessageBinding
    private val sharedViewModel : SharedViewModel by activityViewModels()

    private val newMessageObserver = Observer<ViewModelEvent<Message>> { event ->
        val message = event.getUnhandledContent()
        if (message != null) {
            Toast.makeText(context, "Message Created", Toast.LENGTH_LONG).show()
            super.getActivity()?.onBackPressed()
        }
    }

    private val errorObserver = Observer<ViewModelEvent<String>> { event ->
        val error = event.getUnhandledContent()
        if (error != null) {
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            binding.label.text = error
        }
    }

    private val textWatcher = object: TextWatcher {
        override fun afterTextChanged(s : Editable?) {}
        override fun beforeTextChanged(s : CharSequence?, start: Int, count : Int, after: Int) {}
        override fun onTextChanged(s : CharSequence?, start: Int, before: Int, count: Int) {
            binding.addMessage.isEnabled = (binding.content.text.length > 16) //&& (binding.poster.text.length > 3) && (binding.member.text.length > 5)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.messageDetail.observe(this, newMessageObserver)
        sharedViewModel.error.observe(this, errorObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentNewMessageBinding.inflate(inflater, container, false)
        binding.addMessage.isEnabled = false
        binding.content.addTextChangedListener(textWatcher)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newMessageFragment = this
    }

    fun addMessage() {
        sharedViewModel.addMessage(
            Message(
                UUID.randomUUID().toString(),
                sharedViewModel.member.toString(),
                Clock.System.now(),
                binding.content.text.toString(),
            )
        )
    }
}