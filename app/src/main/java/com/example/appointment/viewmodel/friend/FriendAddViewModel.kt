package com.example.appointment.viewmodel.friend

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class FriendAddViewModel (application: Application) : BaseViewModel(application){
    val searchFriendEmail : MutableLiveData<String?> = MutableLiveData("")
    val searchFriendNickName :MutableLiveData<String?> = MutableLiveData("")
    val searchFriendStatusMessage :MutableLiveData<String?> = MutableLiveData("")
    var searchFriendImgURL :MutableLiveData<String?> = MutableLiveData("")

    var searchFriend :MutableLiveData<Boolean> = MutableLiveData()
    var friendRequestCheck : MutableLiveData<Int> = MutableLiveData(0)

    fun fnSearchFriend(){
        if(searchFriendEmail.value != ""){
            viewModelScope.launch {
                val docRef = firestore.collection("Profile").document(searchFriendEmail.value!!)
                try {
                    val dataMap = utils.readDataFirebase(docRef)
                    if(dataMap != null){
                        searchFriendImgURL.value = dataMap?.get("imgURL") as? String
                        searchFriendNickName.value = dataMap?.get("nickname") as? String
                        searchFriendStatusMessage.value = dataMap?.get("statusmessage") as? String
                        searchFriend.value = true
                    }else {
                        searchFriend.value = false
                    }
                }catch (e: Exception) {
                    Log.e("Coroutine", "Error fetching friend list", e)
                }
            }
        }else{
            searchFriend.value = false
        }
    }

    fun fnSelectFriendRequestItem(nickname: String?){
        val userEmail = auth.currentUser?.email
        if(searchFriendEmail.value != ""){
            if(searchFriendEmail.value == userEmail){
                friendRequestCheck.value = 2
            }else if(searchFriend.value == true){
                val emailReplace = searchFriendEmail.value!!.replace(".","_")
                val reference = database.getReference(emailReplace)
                reference.child("friendRequest").child(nickname!!).setValue(userEmail).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        friendRequestCheck.value = 1
                    }else{
                        friendRequestCheck.value = 4
                    }
                }
            }else{
                friendRequestCheck.value = 3
            }
        }else{
            searchFriend.value = false
        }
    }
}