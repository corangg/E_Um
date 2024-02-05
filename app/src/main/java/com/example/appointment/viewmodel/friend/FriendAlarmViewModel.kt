package com.example.appointment.viewmodel.friend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.model.ChatDataModel
import com.example.appointment.model.FriendRequestAlarmData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener


class FriendAlarmViewModel(application: Application) : AndroidViewModel(application) {
    init {
        fnFriendRequestedList()
    }
    val friendRequestAlarmDataList : MutableLiveData<MutableList<FriendRequestAlarmData>> = MutableLiveData()

    private val utils = Utils()

    fun fnFriendRequestedList() {
        val userEmail = auth.currentUser?.email!!
        val alarmRequestList = mutableListOf<FriendRequestAlarmData>()
        val emailReplace = userEmail.replace(".", "_")
        val reference = database.getReference(emailReplace).child("friendRequest")

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
        })//수정 필요 addListenerForSingleValueEvent모듈화 계속 오류남 나중에 해보자
    }

    fun fnFriendRequestAccept(friendEmail:String, friendNickname: String,nickname: String){
        val userEmail = auth.currentUser?.email!!
        val reference = database.getReference(userEmail.replace(".","_")).child("friendRequest").child(friendNickname)
        val docRef =  firestore.collection("Friend")

        reference.removeValue()

        docRef.document(userEmail).update(friendNickname,friendEmail).addOnSuccessListener {
            fnFriendRequestedList()
        }
        docRef.document(friendEmail).update(nickname,userEmail)
    }

    fun fnFriendRequestRefuse(nickname: String?,email: String?){
        val reference = database.getReference(auth.currentUser?.email.toString().replace(".","_")).child("friendRequest").child(nickname!!)

        reference.removeValue().addOnSuccessListener {
            fnFriendRequestedList()
        }
    }
}