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
import com.example.appointment.repository.FriendAddRepository
import com.example.appointment.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FriendAddViewModel @Inject constructor(
    application: Application,
    private val friendAddRepository: FriendAddRepository) : BaseViewModel(application){
    val searchFriendEmail : MutableLiveData<String?> = MutableLiveData("")
    val searchFriendNickName :MutableLiveData<String?> = MutableLiveData("")
    val searchFriendStatusMessage :MutableLiveData<String?> = MutableLiveData("")

    val searchFriend :MutableLiveData<Boolean> = MutableLiveData()
    val friendRequestCheck : MutableLiveData<Int> = MutableLiveData(0)

    var searchFriendImgURL : String = ""

    fun fnSearchFriend(){
        if(searchFriendEmail.value != ""){
            viewModelScope.launch {
                val docRef = firestore.collection("Profile").document(searchFriendEmail.value!!)
                val dataMap = utils.readDataFirebase(docRef)
                if(dataMap != null){
                    searchFriendImgURL = dataMap.get("imgURL") as String
                    searchFriendNickName.value = dataMap.get("nickname") as? String
                    searchFriendStatusMessage.value = dataMap.get("statusmessage") as? String
                    searchFriend.value = true
                }else {
                    searchFriend.value = false
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
                val reference = database.getReference(emailReplace).child("friendRequest").child(nickname!!)
                viewModelScope.launch {
                    if(friendAddRepository.selectFriendRequestItem(reference)){
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