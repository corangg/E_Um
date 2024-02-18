package com.example.appointment.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.ChatRoomData
import com.example.appointment.data.ProfileDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatFragmentRepository(private val utils: Utils) {
    val chatRoomProfileList: MutableLiveData<MutableList<ChatRoomData>> = MutableLiveData()
    var chatRoomData = mutableListOf<ChatRoomData>()

    fun fetchChatRoomList(friendsProfileList: MutableList<ProfileDataModel>) {
        val userEmail = Utils.auth.currentUser?.email
        val reference = Utils.database.getReference("chat")
        var chatRoomSize: Int = 0

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference) { snapshot ->
                        val emailPath = snapshot.key ?: ""
                        val replaceEmailPath = emailPath.replace("_", ".")
                        val splitArray = replaceEmailPath.split("||")
                        val totalChatCount = snapshot.childrenCount.toInt()

                        if (chatRoomSize == 0) {
                            chatRoomProfileList.value = mutableListOf()
                        }

                        if (userEmail == splitArray[0] || userEmail == splitArray[1]) {
                            fnLastChatSet(emailPath, totalChatCount, userEmail, friendsProfileList)
                            chatRoomSize++
                        }
                    }
                } else {
                    chatRoomProfileList.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ChatFragmentRepository", "Error fetching chat room list", error.toException())
            }
        })
    }

    private fun fnLastChatSet(emailPath: String?, totalChatCount: Int, userEmail: String, friendsProfileList: MutableList<ProfileDataModel>) {
        val reference = Utils.database.getReference("chat").child(emailPath!!)
        chatRoomData = mutableListOf()

        reference.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val firstemail = snapshot.children.first().child("email1").value.toString()
                    val secondeemail = snapshot.children.first().child("email2").value.toString()
                    val firstnickname = snapshot.children.first().child("nickname1").value.toString()
                    val secondenickname = snapshot.children.first().child("nickname2").value.toString()
                    val emil1CheckCount = snapshot.children.first().child("email1MessageCheck").value.toString().toInt()
                    val emil2CheckCount = snapshot.children.first().child("email2MessageCheck").value.toString().toInt()

                    if(firstemail == userEmail){
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val notCheckChatCount = totalChatCount - emil1CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,secondeemail,secondenickname,friendsProfileList)
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }else{
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val notCheckChatCount : Int = totalChatCount - emil2CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,firstemail,firstnickname,friendsProfileList)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fnChatRoomDataAdd(snapshot: DataSnapshot, notCheckChatCount: Int, email: String, nickname: String,friendsProfileList: MutableList<ProfileDataModel>) {
        val lastChatTime = snapshot.children.last().key?.toLong()
        val lastMessage = snapshot.children.last().child("message").value.toString()

        var imgURL: String = ""

        for (i in 0..friendsProfileList.size-1)
        {
            if(nickname==friendsProfileList[i].nickname){
                imgURL = friendsProfileList[i].imgURL
            }
        }

        val chatRoomProfile = ChatRoomData(
            email,
            nickname,
            lastMessage,
            lastChatTime,
            notCheckChatCount,
            imgURL
        )
        chatRoomData.add(chatRoomProfile)
        fnChatProfileArray()
    }

    fun fnChatProfileArray(){
        val sortedList = chatRoomData.sortedByDescending { it.time }
        chatRoomProfileList.value = sortedList.toMutableList()
    }
}