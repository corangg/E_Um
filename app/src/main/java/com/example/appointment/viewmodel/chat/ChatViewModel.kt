package com.example.appointment.viewmodel.chat

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.model.ChatDataModel
import com.example.appointment.repository.ChatRepository
import com.example.appointment.viewmodel.BaseViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    application: Application,
    private val chatRepository: ChatRepository) : BaseViewModel(application){
    val chatData: MutableLiveData<MutableList<ChatDataModel>> = chatRepository.chatData
    val sendMessage:MutableLiveData<String?> = MutableLiveData(null)

    var chatRoomName : String = ""

    fun fnChatMessageSet(chatRoom:String, chatCount:Int?){
        chatRoomName = chatRoom
        chatRepository.chatMessageSet(chatRoom, chatCount)
    }

    fun fnMessageSend(){
        if(sendMessage.value!=null){
            val reference = database.getReference("chat").child(chatRoomName)

            viewModelScope.launch {
                if(chatRepository.messageSend(reference,sendMessage.value!!)){
                    sendMessage.value = null
                }
            }
        }
    }
}