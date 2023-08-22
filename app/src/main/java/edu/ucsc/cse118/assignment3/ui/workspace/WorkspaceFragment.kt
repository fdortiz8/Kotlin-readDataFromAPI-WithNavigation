package edu.ucsc.cse118.assignment3.ui.workspace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ucsc.cse118.assignment3.R
import edu.ucsc.cse118.assignment3.data.Workspace
import edu.ucsc.cse118.assignment3.databinding.FragmentWorkspaceBinding
import edu.ucsc.cse118.assignment3.model.SharedViewModel
import edu.ucsc.cse118.assignment3.model.ViewModelEvent

class WorkspaceFragment : Fragment(), WorkspaceListener {
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView

    private val errorObserver = Observer<ViewModelEvent<String>> { event ->
        val error = event.getUnhandledContent()
        if (error != null) {
            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
        }
    }

    private val workspacesObserver = Observer<ArrayList<Workspace>> { workspaces ->
        recyclerView.adapter = WorkspaceAdapter(workspaces, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel.error.observe(this, errorObserver)
        sharedViewModel.workspaces.observe(this, workspacesObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedViewModel.error.removeObserver(errorObserver)
        sharedViewModel.workspaces.removeObserver(workspacesObserver)
    }

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View {
        val fragmentBinding = FragmentWorkspaceBinding.inflate(inflater, container, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = WorkspaceAdapter(arrayListOf(), this)
        sharedViewModel.getWorkspaces()

        (activity as AppCompatActivity?)!!.supportActionBar!!.title = sharedViewModel.member.value?.name
    }

    override fun onClick(workspace: Workspace) {
        sharedViewModel.setWorkspace(workspace)
        findNavController().navigate(R.id.action_workspaceFragment_to_channelFragment)
    }

}