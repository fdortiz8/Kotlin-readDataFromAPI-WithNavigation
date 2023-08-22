package edu.ucsc.cse118.assignment3.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.ucsc.cse118.assignment3.data.Channel
import edu.ucsc.cse118.assignment3.data.Member
import edu.ucsc.cse118.assignment3.data.Message
import edu.ucsc.cse118.assignment3.data.Workspace
import edu.ucsc.cse118.assignment3.repo.ChannelRepository
import edu.ucsc.cse118.assignment3.repo.MemberRepository
import edu.ucsc.cse118.assignment3.repo.MessageRepository
import edu.ucsc.cse118.assignment3.repo.WorkspaceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val _workspaces = MutableLiveData<ArrayList<Workspace>>()
    val workspaces : LiveData<ArrayList<Workspace>> = _workspaces

    private val _workspace = MutableLiveData<ViewModelEvent<Workspace>>()
    val workspace : LiveData<ViewModelEvent<Workspace>> = _workspace

    private val _channels = MutableLiveData<ArrayList<Channel>>()
    val channels : LiveData<ArrayList<Channel>> = _channels

    private val _channel = MutableLiveData<ViewModelEvent<Channel>>()
    val channel : LiveData<ViewModelEvent<Channel>> = _channel

    private val _messages = MutableLiveData<ArrayList<Message>>()
    val messages : LiveData<ArrayList<Message>> = _messages

    private val _messageDetail = MutableLiveData<ViewModelEvent<Message>>()
    val messageDetail : LiveData<ViewModelEvent<Message>> = _messageDetail

    private val _member = MutableLiveData<Member>()
    val member: LiveData<Member> = _member

    private val _error = MutableLiveData<ViewModelEvent<String>>()
    val error : LiveData<ViewModelEvent<String>> = _error

    fun setWorkspace(value: Workspace) {
        _workspace.value = ViewModelEvent(value)
    }

    fun setChannel(value: Channel) {
        _channel.value = ViewModelEvent(value)
    }
    fun setMessage(value: Message) {
        _messageDetail.value = ViewModelEvent(value)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _member.postValue(MemberRepository().login(email, password))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }

    fun getWorkspaces() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _workspaces.postValue(WorkspaceRepository().getAll(member.value))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }



    fun getChannels() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _channels.postValue(ChannelRepository().getAllChannels(member.value, workspace.value))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }

    fun getChannel() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _channel.postValue(ViewModelEvent(_channel.value?.let { ChannelRepository().getOneChannel(member.value, _workspace.value, it.getRawContent()) }) as ViewModelEvent<Channel>?)
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }

    fun getMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _messages.postValue(MessageRepository().getAllMessages(member.value, channel.value))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }

    fun getWorkspace() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _workspace.postValue(ViewModelEvent(WorkspaceRepository().getOne(member.value, _workspace.value?.getRawContent())))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }

    fun getMessageDetail() {
        viewModelScope.launch(Dispatchers.IO) {
            try{
                _messageDetail.postValue(ViewModelEvent(MessageRepository().getOneMessage(member.value, _messageDetail.value?.getRawContent())) )
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }


    fun addMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                MessageRepository().addOneMessage(member.value, channel ,message)
                _messageDetail.postValue(ViewModelEvent(message))
            } catch (e: Exception) {
                _error.postValue(ViewModelEvent(e.message.toString()))
            }
        }
    }
}