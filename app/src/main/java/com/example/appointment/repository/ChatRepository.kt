package com.example.appointment.repository

import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.model.ChatDataModel
import com.google.firebase.database.DatabaseReference
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChatRepository(private val utils: Utils) {
    val chatData: MutableLiveData<MutableList<ChatDataModel>> = MutableLiveData()

    fun chatMessageSet(chatRoom:String , chatCount:Int?){
        val profileList = mutableListOf<ChatDataModel>()
        val reference = Utils.database.getReference("chat").child(chatRoom)
        var messageCount:Int = 0
        var chatRoomInfoName : String = ""

        utils.readDataFRDAddChildEventListener(reference){
            if(messageCount ==0){
                chatRoomInfoName = it.key!!
                messageCount = messageCount + 1
            }else{
                val time = it.key
                val profileData = ChatDataModel(
                    it.child("senderemail").value.toString(),
                    time!!,
                    it.child("message").value.toString(),
                    messageCount
                )
                chatMessageCheckCountSet(chatRoom, messageCount, chatRoomInfoName)
                profileList.add(profileData)
                messageCount = messageCount + 1

                if(messageCount >= chatCount!!){
                    chatData.value = profileList
                }
            }
        }
    }

    private fun chatMessageCheckCountSet(chatRoom:String, messageCount:Int, chatRoomInfoName : String){
        val reference = Utils.database.getReference("chat").child(chatRoom)
        val chatRoomNameReplace = chatRoom.replace("_",".")
        val chatRoomNameSplitArray = chatRoomNameReplace.split("||")
        val userEmail = auth.currentUser?.email

        if(userEmail == chatRoomNameSplitArray[0]){
            reference.child(chatRoomInfoName).child("email1MessageCheck").setValue(messageCount)
        }else if(userEmail == chatRoomNameSplitArray[1]){
            reference.child(chatRoomInfoName).child("email2MessageCheck").setValue(messageCount)
        }
    }

    suspend fun messageSend(reference: DatabaseReference, sendMessage : String) : Boolean{
        val userEmail = auth.currentUser?.email
        val dataMap = mapOf(
            "senderemail" to userEmail,
            "message" to sendMessage
        )
        return suspendCoroutine {
            reference.child(utils.fnGetCurrentTimeString()).setValue(dataMap).addOnCompleteListener  { task->
                if(task.isSuccessful){
                    it.resume(true)
                }else{
                    it.resume(false)
                }
            }
        }
    }
}