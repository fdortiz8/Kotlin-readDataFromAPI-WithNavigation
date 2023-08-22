package edu.ucsc.cse118.assignment3.ui.workspace

import edu.ucsc.cse118.assignment3.data.Workspace

interface WorkspaceListener {
    fun onClick(workspace: Workspace)
}