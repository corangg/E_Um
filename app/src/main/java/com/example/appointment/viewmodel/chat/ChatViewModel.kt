package com.example.appointment.viewmodel.chat

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.model.ChatDataModel
import com.example.appointment.viewmodel.BaseViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.Date
import java.util.Locale

class ChatViewModel(application: Application) : BaseViewModel(application){
    var chatRoomName: MutableLiveData<String?> = MutableLiveData("")
    var chatData: MutableLiveData<MutableList<ChatDataModel>?> = MutableLiveData()
    var chatRoomInfoName : MutableLiveData<String> = MutableLiveData("")
    var sendMessage:MutableLiveData<String?> = MutableLiveData(null)
    var profileNickname: MutableLiveData<String?> = MutableLiveData("")

    fun fnChatMessageSet(chatRoom:String?, chatCount:Int?){
        val profileList = mutableListOf<ChatDataModel>()
        val reference = database.getReference("chat").child(chatRoom!!)
        var messageCount:Int = 0
        chatRoomName.value = chatRoom

        utils.readDataFRDAddChildEventListener(reference){
            if(messageCount ==0){
                chatRoomInfoName.value = it.key
                messageCount = messageCount + 1
            }else{
                val time = it.key
                val profileData = ChatDataModel(
                    it.child("senderemail").value.toString(),
                    time!!,
                    it.child("message").value.toString(),
                    messageCount
                )
                fnChatMessageCheckCountSet(chatRoom,messageCount)
                profileList.add(profileData)
                messageCount = messageCount + 1

                if(messageCount >= chatCount!!){
                    chatData.value = profileList
                }
            }
        }
    }

    fun fnChatMessageCheckCountSet(chatRoom:String, messageCount:Int){
        val reference = database.getReference("chat").child(chatRoom)
        val chatRoomNameReplace = chatRoom.replace("_",".")
        val chatRoomNameSplitArray = chatRoomNameReplace.split("||")

        if(userEmail == chatRoomNameSplitArray[0]){
            reference.child(chatRoomInfoName.value!!).child("email1MessageCheck").setValue(messageCount)
        }else if(userEmail == chatRoomNameSplitArray[1]){
            reference.child(chatRoomInfoName.value!!).child("email2MessageCheck").setValue(messageCount)
        }
    }

    fun fnMessageSend(){
        if(sendMessage.value!=null){
            val reference = database.getReference("chat").child(chatRoomName.value!!)

            val dataMap = mapOf(
                "senderemail" to userEmail,
                "message" to sendMessage.value
            )
            reference.child(utils.fnGetCurrentTimeString()).setValue(dataMap).addOnCompleteListener  { task->
                if(task.isSuccessful){
                    sendMessage.value = null
                }
            }
        }
    }
}