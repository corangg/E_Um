package com.example.appointment.viewmodel.friend

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.model.FriendRequestAlarmData
import com.example.appointment.repository.FriendAlarmRepository
import com.example.appointment.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FriendAlarmViewModel @Inject constructor(
    application: Application,
    private val friendAlarmRepository: FriendAlarmRepository) : BaseViewModel(application) {
    init {
        friendRequestedList()
    }
    val friendRequestAlarmDataList : MutableLiveData<MutableList<FriendRequestAlarmData>> = friendAlarmRepository.friendRequestAlarmDataList

    fun friendRequestedList() {
        friendAlarmRepository.getFriendRequestList()
    }

    fun friendRequestAccept(friendEmail:String, friendNickname: String, nickname: String){
        val userEmail = auth.currentUser?.email!!
        val reference = database.getReference(userEmail.replace(".","_")).child("friendRequest").child(friendNickname)
        friendAlarmRepository.updateFriendData(reference, friendEmail, friendNickname, nickname)
    }

    fun friendRequestRefuse(nickname: String?, email: String?){
        val reference = database.getReference(auth.currentUser?.email.toString().replace(".","_")).child("friendRequest").child(nickname!!)
        friendAlarmRepository.friendRequestDelete(reference)
    }
}