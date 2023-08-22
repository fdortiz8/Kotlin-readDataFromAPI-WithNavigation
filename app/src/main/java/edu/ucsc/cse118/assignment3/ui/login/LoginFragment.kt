package edu.ucsc.cse118.assignment3.ui.login

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
import androidx.navigation.fragment.findNavController
import edu.ucsc.cse118.assignment3.R
import edu.ucsc.cse118.assignment3.data.Member
import edu.ucsc.cse118.assignment3.databinding.FragmentLoginBinding
import edu.ucsc.cse118.assignment3.model.SharedViewModel
import edu.ucsc.cse118.assignment3.model.ViewModelEvent


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val memberObserver = Observer<Member> { member ->
        binding.password.text.clear()
        if (member.role == "member")
            findNavController().navigate(R.id.action_loginFragment_to_workspaceFragment)
        else
            findNavController().navigate(R.id.action_loginFragment_to_workspaceFragment)
    }

    private val errorObserver = Observer<ViewModelEvent<String>> { event ->
        val error = event.getUnhandledContent()
        if (error != null) {
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
            binding.label.text = error
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s : Editable?) {}
        override fun beforeTextChanged(s : CharSequence?, start: Int , count : Int, after: Int) {}
        override fun onTextChanged(s : CharSequence?, start: Int, before: Int, count: Int) {
            binding.login.isEnabled = (binding.email.text.length > 4) && (binding.password.text.length > 4)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.member.observe(this, memberObserver)
        sharedViewModel.error.observe(this, errorObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.member.removeObserver(memberObserver)
        sharedViewModel.error.removeObserver(errorObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.login.isEnabled = false
        binding.email.addTextChangedListener(textWatcher)
        binding.password.addTextChangedListener(textWatcher)
        return binding.root
    }

    override fun onViewCreated(view : View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginFragment = this
    }

    fun login() {
        sharedViewModel.login(binding.email.text.toString(), binding.password.text.toString())
    }
}






















