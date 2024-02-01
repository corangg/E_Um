package com.example.appointment.viewmodel.chat

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.FirebaseCertified.Companion.auth
import com.example.appointment.model.ChatDataModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChatViewModel(application: Application)  : AndroidViewModel(application){
    var chatRoomName: MutableLiveData<String?> = MutableLiveData("")
    var chatData: MutableLiveData<MutableList<ChatDataModel>?> = MutableLiveData()
    var chatRoomInfoName : MutableLiveData<String> = MutableLiveData("")

    val database = FirebaseDatabase.getInstance()



    fun fnChatMessageSet(chatRoom:String?, chatCount:Int?){
        val profileList = mutableListOf<ChatDataModel>()
        val reference = database.getReference("chat").child(chatRoom!!)
        var messageCount:Int = 0

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                if(messageCount ==0){
                    chatRoomInfoName.value = snapshot.key
                    messageCount = messageCount + 1
                }else{
                    val time = snapshot.key
                    val profileData = ChatDataModel(
                        snapshot.child("senderemail").value.toString(),
                        time!!,
                        snapshot.child("message").value.toString(),
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

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading data", error.toException())
            }
        })

    }

    fun fnChatMessageCheckCountSet(chatRoom:String, messageCount:Int){
        val reference = database.getReference("chat").child(chatRoom)
        val chatRoomNameReplace = chatRoom.replace("_",".")
        val chatRoomNameSplitArray = chatRoomNameReplace.split("||")

        if(auth.currentUser!!.email == chatRoomNameSplitArray[0]){
            reference.child(chatRoomInfoName.value!!).child("email1MessageCheck").setValue(messageCount)
        }else if(auth.currentUser!!.email == chatRoomNameSplitArray[1]){
            reference.child(chatRoomInfoName.value!!).child("email2MessageCheck").setValue(messageCount)
        }
    }
}