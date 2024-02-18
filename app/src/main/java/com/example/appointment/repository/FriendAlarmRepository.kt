package com.example.appointment.repository

import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.FriendRequestAlarmData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class FriendAlarmRepository(private val utils: Utils) {
    val friendRequestAlarmDataList : MutableLiveData<MutableList<FriendRequestAlarmData>> = MutableLiveData()

    fun getFriendRequestList() {
        val alarmRequestList = mutableListOf<FriendRequestAlarmData>()
        val userEmail = auth.currentUser?.email!!
        val emailReplace = userEmail.replace(".", "_")
        val reference = Utils.database.getReference(emailReplace).child("friendRequest")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference) {
                        val sendernickname = it.key
                        val senderEmail = it.getValue(String::class.java)

                        val friendRequestData = FriendRequestAlarmData(
                            senderEmail!!,
                            sendernickname!!
                        )
                        alarmRequestList.add(friendRequestData)
                        friendRequestAlarmDataList.value = alarmRequestList
                    }
                } else {
                    friendRequestAlarmDataList.value = alarmRequestList
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun updateFriendData(reference: DatabaseReference, friendEmail:String, friendNickname: String, nickname: String){
        val docRef =  Utils.firestore.collection("Friend")
        val userEmail = auth.currentUser?.email
        reference.removeValue()
        docRef.document(userEmail!!).update(friendNickname,friendEmail).addOnSuccessListener{
            getFriendRequestList()
        }
        docRef.document(friendEmail).update(nickname,userEmail)
    }

    fun friendRequestDelete(reference: DatabaseReference){
        reference.removeValue().addOnSuccessListener {
            getFriendRequestList()
        }
    }
}