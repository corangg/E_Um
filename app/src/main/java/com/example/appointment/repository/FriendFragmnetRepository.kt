package com.example.appointment.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils
import com.example.appointment.data.ProfileDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference

class FriendFragmnetRepository(private val utils: Utils) {
    private val userEmail = Utils.auth.currentUser?.email
    val userEmailReplace = userEmail!!.replace(".", "_")
    val friendRequestAlarmStatus : MutableLiveData<Boolean> = MutableLiveData()

    fun getFriendRequestAlarmData() {
        val reference = Utils.database.getReference(userEmailReplace).child("friendRequest")
        reference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    friendRequestAlarmStatus.value = true
                }else{
                    friendRequestAlarmStatus.value = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    suspend fun getFriendListData(docRef : DocumentReference): List<String> {
        val friendList = mutableListOf<String>()

        try {
            val dataMap = utils.readDataFirebase(docRef)
            if (dataMap != null) {
                for ((_, value) in dataMap) {
                    friendList.add(value.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("Coroutine", "Error fetching friend list", e)
        }

        return friendList
    }

    suspend fun getFriendProfileData(friendEmails: List<String>): MutableList<ProfileDataModel> {
        val profileList = mutableListOf<ProfileDataModel>()

        for (friendEmail in friendEmails) {
            val docRef = Utils.firestore.collection("Profile").document(friendEmail)
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null) {
                    val profileData = ProfileDataModel(
                        dataMap["email"] as String,
                        dataMap["nickname"] as String,
                        dataMap["statusmessage"] as String,
                        "",
                        dataMap["imgURL"] as String
                    )
                    profileList.add(profileData)
                }
            } catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend profile", e)
            }
        }
        return profileList
    }
}