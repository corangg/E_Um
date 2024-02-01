package com.example.appointment.viewmodel

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.AddressDBHelper

import com.example.appointment.FirebaseCertified
import com.example.appointment.apiservice.NaverGeocode
import com.example.appointment.apiservice.NaverKeWord
import com.example.appointment.apiservice.NaverReverseGeocode

import com.example.appointment.R
import com.example.appointment.apiservice.TMapCarRoute
import com.example.appointment.apiservice.TMapPublicTransportRoute
import com.example.appointment.apiservice.TMapWalkRoute
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.CarRouteRequest
import com.example.appointment.model.CarRouteResponse

import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.ChatDataModel
import com.example.appointment.model.ChatRoomData
import com.example.appointment.model.FriendRequestAlarmData

import com.example.appointment.model.KeyWordResponse

import com.example.appointment.model.PlaceItem
import com.example.appointment.model.GeocodingRespone
import com.example.appointment.model.ReverseGeocodingResponse
import com.example.appointment.model.TransitRouteRequest
import com.example.appointment.model.PublicTransportRouteResponse
import com.example.appointment.model.ScheduleSet
import com.example.appointment.model.WalkRouteRequest
import com.example.appointment.model.WalkRouteResponse

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date


class MainViewmodel(application: Application) : AndroidViewModel(application){
    var selectFragment : MutableLiveData<String> = MutableLiveData("")
    var profileImgURL :MutableLiveData<String?> = MutableLiveData("")
    var profileNickname: MutableLiveData<String?> = MutableLiveData("")
    var profileStatusMessage: MutableLiveData<String?> = MutableLiveData("")
    var profileName: MutableLiveData<String?> = MutableLiveData("")
    var profilePhone: MutableLiveData<String?> = MutableLiveData("")
    var profileEmail: MutableLiveData<String?> = MutableLiveData("")
    var profilePassword: MutableLiveData<String?> = MutableLiveData("")
    var profilePasswordCheck: MutableLiveData<String?> = MutableLiveData("")
    var profileAddress: MutableLiveData<String?> = MutableLiveData("")
    var newpassword :MutableLiveData<String?> = MutableLiveData("")
    var newpasswordCheck :MutableLiveData<String?> = MutableLiveData("")
    var searchFriendEmail :MutableLiveData<String?> = MutableLiveData("")
    var searchFriendNickName :MutableLiveData<String?> = MutableLiveData("")
    var searchFriendStatusMessage :MutableLiveData<String?> = MutableLiveData("")
    var searchFriendImgURL :MutableLiveData<String?> = MutableLiveData("")
    var searchFriend :MutableLiveData<Boolean> = MutableLiveData()
    var resultstatusmessage :MutableLiveData<String?> = MutableLiveData("")
    var friendImgURL : MutableLiveData<String?> = MutableLiveData("")
    var scheduleHH : MutableLiveData<String> = MutableLiveData("")
    var scheduleMM : MutableLiveData<String> = MutableLiveData("")
    var scheduleAlarmHH : MutableLiveData<String> = MutableLiveData("")
    var scheduleAlarmMM : MutableLiveData<String> = MutableLiveData("")
    var scheduleDate : MutableLiveData<String> = MutableLiveData("")
    var scheduleYYYYMMDD : MutableLiveData<String> = MutableLiveData("")
    var searchKewordAndAddress : MutableLiveData<String?> = MutableLiveData("")
    var kewordName : MutableLiveData<String?> = MutableLiveData("")
    var selectAddress : MutableLiveData<String?> = MutableLiveData("")
    var selectRoadAddress : MutableLiveData<String?> = MutableLiveData("")
    var startX:MutableLiveData<String> = MutableLiveData("")
    var startY:MutableLiveData<String> = MutableLiveData("")
    var endX:MutableLiveData<String> = MutableLiveData("")
    var endY:MutableLiveData<String> = MutableLiveData("")
    var publicTransportTime :MutableLiveData<String> = MutableLiveData("")
    var carTime :MutableLiveData<String> = MutableLiveData("")
    var walkTime : MutableLiveData<String> = MutableLiveData("")
    var meetingPlaceAddress :MutableLiveData<String> = MutableLiveData("")
    var chatRoomFriendNickName:MutableLiveData<String?> = MutableLiveData("")
    var sendMessage:MutableLiveData<String?> = MutableLiveData(null)
    var chatRoomProfileURL:MutableLiveData<String?> = MutableLiveData("")
    var chatRoomInfoName : MutableLiveData<String> = MutableLiveData("")
    var chatRoomName:MutableLiveData<String?> = MutableLiveData("")
    var scheduleDeleteText : MutableLiveData<String> = MutableLiveData()
    var selectScheduleNickname : MutableLiveData<String> = MutableLiveData()
    var scheduleEmailPath : MutableLiveData<String> = MutableLiveData()

    var openGallery :MutableLiveData<Boolean> = MutableLiveData(false)
    var imageUpload :MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordEdit :MutableLiveData<Boolean> = MutableLiveData(false)
    var addressEditActivityStart :MutableLiveData<Boolean> = MutableLiveData(false)
    var logOutSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    var passwordCheckSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    var nickNameEditActivityStart:MutableLiveData<Boolean> = MutableLiveData(false)
    var nickNameEditSave:MutableLiveData<Boolean> = MutableLiveData(false)
    var friendDeleteSuccess:MutableLiveData<Boolean> = MutableLiveData(false)
    var startChatActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleCalendarFragmentStart : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleSetFragmentStart : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleAmPmSet : MutableLiveData<Boolean> = MutableLiveData(true)
    var scheduleMapActivityStart : MutableLiveData<Boolean> = MutableLiveData(false)
    var showKeywordList : MutableLiveData<Boolean> = MutableLiveData(false)
    var showSelectKeywordTab : MutableLiveData<Boolean> = MutableLiveData(false)
    var showSelectMarker : MutableLiveData<Boolean> = MutableLiveData(false)
    var mapActivityEnd : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleAcceptOK : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleEditSuccess : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleListDelete : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleAlarmCheck : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleRefuseOkView : MutableLiveData<Boolean> = MutableLiveData()
    var friendAlarmReceiveStatus :MutableLiveData<Boolean> = MutableLiveData()

    var accountError: MutableLiveData<Int> = MutableLiveData(-1)
    var friendRequestCheck : MutableLiveData<Int> = MutableLiveData(0)
    var addressDel :MutableLiveData<Int> = MutableLiveData()
    var passwordUpdate :MutableLiveData<Int> = MutableLiveData(-1)
    var chatCount : MutableLiveData<Int?> = MutableLiveData(0)
    var publicTransportCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var carCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var walkCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var startCheckAlarmTime : MutableLiveData<Int> = MutableLiveData(5)
    var selectPosition : MutableLiveData<Int> = MutableLiveData(-1)
    var selectTransport : MutableLiveData<Int> = MutableLiveData(0)

    var friendsProfileList: MutableLiveData<MutableList<ProfileDataModel>?> = MutableLiveData()
    var chatRoomProfileArray:MutableLiveData<MutableList<ChatRoomData>?> = MutableLiveData()
    var chatProfileArray : MutableLiveData<MutableList<String?>> = MutableLiveData()
    var chatData: MutableLiveData<MutableList<ChatDataModel>?> = MutableLiveData()
    var scheduleDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData()
    var searchKewordList : MutableLiveData<MutableList<PlaceItem>> = MutableLiveData()
    var friendRequestAlarmDataList : MutableLiveData<MutableList<FriendRequestAlarmData>> = MutableLiveData()
    var scheduleAlarmDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData(mutableListOf())

    var selectFriendProfile : MutableLiveData<ProfileDataModel> = MutableLiveData()
    var searchCoordinate : MutableLiveData<LatLng> = MutableLiveData(null)

    var chatRoomData = mutableListOf<ChatRoomData>()

    var context = getApplication<Application>().applicationContext

    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val storage = FirebaseStorage.getInstance()
    val database = FirebaseDatabase.getInstance()

    init {
         addressDel.value = R.id.menu_edit
    }

    companion object{
        const val KEWORD_BASE_URL = "https://openapi.naver.com/"
        const val KEWORD_CLIENT_ID = "a3vo4TcuBHWsYnQM5piZ"
        const val KEWORD_API_KEY = "ykLrkfuepo"

        const val NAVER_BASE_URL = "https://naveropenapi.apigw.ntruss.com/"
        const val NAVER_CLIENT_ID = "tsyu4npcze"
        const val NAVER_API_KEY = "cPhAjqfTinr2sIqCwb4SGfn9CjHBNFfIOoV7iYub"

        const val TMAP_BASE_URL = "https://apis.openapi.sk.com"
        const val TMAP_API_KEY = "VHK847GcwI6nhPfm8qYFO8x15oJl4IuM3L9ms1cl"
    }

    fun fnBottomNavigationItemSelected(item : MenuItem):Boolean{
        when(item.itemId){
            R.id.navigation_profile->{
                selectFragment.value = "profile"
                return true
            }
            R.id.navigation_friends->{
                selectFragment.value = "friends"
                return true
            }
            R.id.navigation_chat->{
                chatRoomData = mutableListOf()
                selectFragment.value = "chat"
                return true
            }
            R.id.navigation_schedule->{
                selectFragment.value = "schedule"
                return true
            }
        }
        return false
    }

    fun fnProfileAddressEdit(){
        addressEditActivityStart.value = true
        addressEditActivityStart.value = false
    }

    fun fnLogout(){
        FirebaseCertified.auth.signOut()
        FirebaseCertified.email = null
        logOutSuccess.value = true
    }

    fun fnAddressCombine(zonecode:String?, address:String?){
        val combineAddress = "$zonecode,"+ "$address"
        profileAddress.value = combineAddress
    }

    fun fnGetPrivacyData() {
        val user = auth.currentUser

        if (user != null) {
            val userEmail: String? = user.email
            val docRef = firestore.collection("Privacy").document(userEmail.toString())

            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data

                    profileEmail.value = dataMap?.get("email") as? String
                    profileName.value = dataMap?.get("name") as? String
                    profileNickname.value = dataMap?.get("nickname") as? String
                    profilePhone.value = dataMap?.get("phoneNumber") as? String
                    profilePassword.value = dataMap?.get("password") as? String
                    fnAddressCombine(dataMap?.get("zonecode") as? String,dataMap?.get("address") as? String)

                } else {
                    accountError.value = 0
                }
            }
        }
    }

    fun fnGetProfileData(){
        val user = auth.currentUser

        if (user != null) {
            val userEmail: String? = user.email
            val docRef = firestore.collection("Profile").document(userEmail.toString())

            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data

                    profileImgURL.value = dataMap?.get("imgURL") as? String
                    profileStatusMessage.value = dataMap?.get("statusmessage") as? String
                    resultstatusmessage.value
                    imageUpload.value = true

                } else {
                    accountError.value = 1
                }
            }
        }

    }

    fun fnProfileEdit(){
        firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("nickname",profileNickname.value.toString())
        firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("statusmessage",profileStatusMessage.value.toString())
    }

    fun fnPrivacyEdit(){
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("zonecode",fnSplitAddress()[0])
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("nickname",profileNickname.value.toString())
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("address",fnSplitAddress()[1])
    }

    fun fnNickNameEdit(){
        nickNameEditActivityStart.value = true
    }

    fun fnNickNameSave(){
        nickNameEditSave.value = true
    }

    fun fnNicknameEditDataSet(nickname:String?, statusmessage:String?){
        profileNickname.value = nickname
        profileStatusMessage.value = statusmessage

    }

    fun fnPasswordEdit(){
        passwordEdit.value = true
    }

    fun fnPasswordCheck(){
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val dataMap = documentSnapshot.data
                profilePassword.value = dataMap?.get("password") as? String
                if(profilePasswordCheck.value == profilePassword.value){
                    passwordCheckSuccess.value = true
                    passwordUpdate.value = 0

                }else{
                    passwordUpdate.value = 1
                }
            }else {
                accountError.value = 0
            }
        }
    }

    fun fnPasswordSave(){
        val currentuser = auth.currentUser

        if(passwordCheckSuccess.value == true){
            if(newpassword.value == newpasswordCheck.value) {
                currentuser?.updatePassword(newpassword.value.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("password", newpassword.value.toString())
                            passwordUpdate.value = 4
                        }else{
                            passwordUpdate.value = 5

                            val exception = task.exception
                            if (exception != null) {
                                val errorMessage = exception.message
                                println("Password update failed: $errorMessage")
                            }
                        }
                    }
            }else{
                passwordUpdate.value = 3
            }
        }else{
            passwordUpdate.value = 2
        }
    }

    fun fnAddressDelete(recyclePosition:Long) {
        val dbHelper = AddressDBHelper(context, auth.currentUser?.email)
        dbHelper.deleteRow(recyclePosition)
    }

    fun fnSplitAddress():List<String>{
        val splitArray = profileAddress.value!!.split(",")
        return splitArray
    }

    fun fnOpenGallery(){
        openGallery.value = true
        openGallery.value = false
    }

    fun fnProfileImageUpdate(photoUri:Uri?){
        if(photoUri!=null){
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
            var storagePath = storage.reference.child("userProfile").child(auth.currentUser?.email.toString()).child("profileimg")

            storagePath.putFile(photoUri!!).continueWithTask{
                return@continueWithTask storagePath.downloadUrl
            }.addOnCompleteListener{downloadUrl->
                firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("imgURL",downloadUrl.result.toString())
                firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("timestamp",timestamp)
            }
        }
    }

    fun fnSearchFriend(){
        if(searchFriendEmail.value != ""){
            val docRef = firestore.collection("Profile").document(searchFriendEmail.value!!)

            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data

                    searchFriendImgURL.value = dataMap?.get("imgURL") as? String
                    searchFriendNickName.value = dataMap?.get("nickname") as? String
                    searchFriendStatusMessage.value = dataMap?.get("statusmessage") as? String
                    searchFriend.value = true

                } else {
                    searchFriend.value = false
                }
            }
        }else{
            searchFriend.value = false
        }
    }

    fun fnFriendRequest(nickname: String?){
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
                        val exception = task.exception
                        if (exception != null) {
                            val errorMessage = exception.message
                            println("Password update failed: $errorMessage")
                        }
                    }
                }
            }else{
                friendRequestCheck.value = 3
            }
        }else{
            searchFriend.value = false
        }
    }

    fun fnFriendAlarmReceive() {
        val userEmail = auth.currentUser?.email!!
        val alarmRequestList = mutableListOf<FriendRequestAlarmData>()
        val emailReplace = userEmail.replace(".", "_")
        val reference = database.getReference(emailReplace)

        reference.child("friendRequest").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    friendRequestAlarmDataList.value = alarmRequestList
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.child("friendRequest").addChildEventListener(object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val sendernickname = snapshot.key
            val senderEmail = snapshot.getValue(String::class.java)

            val FriendRequestData = FriendRequestAlarmData(
                senderEmail!!,
                sendernickname!!
            )
            alarmRequestList.add(FriendRequestData)
            friendRequestAlarmDataList.value = alarmRequestList
            friendAlarmReceiveStatus.value = true

        }
            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                friendAlarmReceiveStatus.value = false
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading data", error.toException())
            }
        })
    }

    fun fnFriendRequestAccept(friendEmail:String, friendNickname: String,nickname: String){
        val userEmail = auth.currentUser?.email!!

        val reference = database.getReference(userEmail.replace(".","_")).child("friendRequest").child(friendNickname)//여기가 문제
        reference.removeValue()

        firestore.collection("Friend").document(userEmail).update(friendNickname!!,friendEmail).addOnSuccessListener {
            Log.d("friendError", "DocumentSnapshot successfully written!")
            fnFriendAlarmReceive()
        }.addOnFailureListener { e ->
            Log.d("friendError", "Error writing document", e)
        }
        firestore.collection("Friend").document(friendEmail).update(nickname,userEmail).addOnSuccessListener {//본인 닉네임
            Log.d("friendError", "DocumentSnapshot successfully written!")
        }.addOnFailureListener { e ->
            Log.d("friendError", "Error writing document", e)
        }
    }


    fun fnFriendRequestRefuse(nickname: String?,email: String?){
        val reference = database.getReference(auth.currentUser?.email.toString().replace(".","_")).child("friendRequest").child(nickname!!)
        reference.removeValue().addOnSuccessListener {
            fnFriendAlarmReceive()
        }
    }


    fun fnFriendList(){
        viewModelScope.launch {
            val user = auth.currentUser
            val friendList = mutableListOf<String>()
            if (user != null) {
                val userEmail: String? = user.email
                val docRef = firestore.collection("Friend").document(userEmail!!)

                try{
                    val documentSnapshot = withContext(Dispatchers.IO) {
                        docRef.get().await()
                }
                    withContext(Dispatchers.Main){
                        if (documentSnapshot.exists()) {
                            val dataMap = documentSnapshot.data

                            if(dataMap!=null){
                                for ((key, value) in dataMap){
                                    dataMap.let {
                                        friendList.add(value.toString())
                                    }
                                }
                                fnFriendProfile(friendList)
                            }
                        }
                    }
            }catch (e: Exception) {
                    Log.e("Coroutine", "Error getting friend profile", e)
                }
            }
        }
    }

    fun fnFriendProfile(friendEmail:MutableList<String>){

        viewModelScope.launch {
            val user = auth.currentUser
            val profileList = mutableListOf<ProfileDataModel>()
            if (user != null) {
                for(i in friendEmail){
                    val docRef = firestore.collection("Profile").document(i)
                    try {
                        val documentSnapshot = withContext(Dispatchers.IO) {
                            docRef.get().await()
                        }
                        withContext(Dispatchers.Main){
                            Log.d("Coroutine", "UI 업데이트 진행 중")
                            if (documentSnapshot.exists()) {
                                val dataMap = documentSnapshot.data
                                val profileData = ProfileDataModel(dataMap?.get("email") as String,
                                    dataMap?.get("nickname") as String,
                                    dataMap?.get("statusmessage") as String,
                                "",
                                    dataMap?.get("imgURL") as String)
                                profileList.add(profileData)

                            } else {
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Coroutine", "Error getting friend profile", e)
                    }
                }
                friendsProfileList.value = profileList
            }
        }
    }

    fun fnFriendDelete(){
        val user = auth.currentUser

        if (user != null) {
            val userEmail: String = user.email!!
            val docRef = firestore.collection("Friend").document(userEmail)
            val updates = HashMap<String, Any>()
            updates[selectFriendProfile.value!!.nickname] = FieldValue.delete()
            docRef.update(updates)

            val friendEmail = selectFriendProfile.value!!.email
            val friendDocRef = firestore.collection("Friend").document(friendEmail)
            val friendUpdates = HashMap<String, Any>()
            friendUpdates[profileNickname.value!!] = FieldValue.delete()
            friendDocRef.update(friendUpdates)

            val userEmailReplace = userEmail!!.replace(".", "_")
            val friendEmailReplace = friendEmail!!.replace(".", "_")
            val chatName : String = userEmailReplace + "||" + friendEmailReplace
            val reversedchatName = chatName.split("||").reversed().joinToString("||")
            val chatReference = database.getReference("chat")
            val appointmentReference = database.getReference("appointment")

            chatReference.child(chatName).removeValue().addOnSuccessListener {
                appointmentReference.child(chatName).removeValue()
            }

            chatReference.child(reversedchatName).removeValue().addOnSuccessListener {
                appointmentReference.child(reversedchatName).removeValue()
            }

            fnFriendList()

            friendDeleteSuccess.value = true
            friendDeleteSuccess.value = false


            chatReference.child(chatName).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        chatReference.child(chatName).removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ChatDeleteError","error",error.toException())
                }
            })

        }
    }

    fun fnFriendProfileSet(){
        friendImgURL.value=selectFriendProfile.value!!.imgURL
    }

    fun chatFriend(){
        startChatActivity.value = true
        startChatActivity.value = false
    }

    fun fnChatStart(friendEmail: String?){

        val userEmail = auth.currentUser?.email!!
        val userEmailReplace = userEmail!!.replace(".", "_")
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val reference = database.getReference("chat")
        val chatName : String = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = chatName.split("||").reversed().joinToString("||")

        reference.child(chatName).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    chatCount.value = snapshot.childrenCount.toInt()
                    chatRoomName.value=chatName
                    chatFriend()
                } else {
                    reference.child(reversedchatName).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                chatCount.value = snapshot.childrenCount.toInt()
                                chatRoomName.value = reversedchatName
                                chatFriend()
                            } else {
                                val chatPath = reference.child(reversedchatName).child(fnGetCurrentTimeString())
                                val dataMap = mapOf(
                                    "email1" to userEmail,
                                    "email2" to friendEmail,
                                    "nickname1" to profileNickname.value,
                                    "nickname2" to selectFriendProfile.value!!.nickname,
                                    "email1MessageCheck" to "0",
                                    "email2MessageCheck" to "0"
                                )
                                chatPath.setValue(dataMap).addOnSuccessListener {
                                    chatCount.value = snapshot.childrenCount.toInt()
                                    chatRoomName.value = reversedchatName
                                    chatFriend()
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.d("ChatStartError","error",error.toException())
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("ChatStartError","error",error.toException())
            }
        })
    }

    fun fnMessageSend(){
        val user = auth.currentUser
        if(sendMessage.value!=null){
            val reference = database.getReference("chat").child(chatRoomName.value!!)

            val dataMap = mapOf(
                "senderemail" to user?.email.toString(),
                "sendernickname" to profileNickname.value,
                "message" to sendMessage.value
            )
            reference.child(fnGetCurrentTimeString()).setValue(dataMap).addOnCompleteListener  { task->
                if(task.isSuccessful){
                    sendMessage.value = null
                }
            }
        }
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

    fun fnGetCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }


    fun fnChatRoomList(){
        val reference = database.getReference("chat")
        val email = auth.currentUser?.email.toString()
        var chatRoomSize : Int = 0

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    //friendRequestAlarmDataList.value = alarmRequestList
                    chatRoomProfileArray.value = mutableListOf()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val emailPath = snapshot.key
                val friendEmail = emailPath?.replace("_",".")
                var splitArray = friendEmail?.split("||")
                //fnNotCheckChatCount(emailPath!!)
                val totalChatCount = snapshot.childrenCount.toInt()

                if(email == splitArray!![0]){
                    chatProfile(emailPath,totalChatCount)
                    chatRoomSize = chatRoomSize + 1

                }else if(email == splitArray[1]){
                    chatProfile(emailPath,totalChatCount)
                    chatRoomSize = chatRoomSize + 1
                }
                if(chatRoomSize == 0){
                    chatRoomProfileArray.value = mutableListOf()
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

    fun chatProfile(emailPath:String?,totalChatCount:Int){

        val reference = database.getReference("chat").child(emailPath!!)
        chatRoomData = mutableListOf()


        reference.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val firstemail = snapshot.children.first().child("email1").value
                    val secondeemail = snapshot.children.first().child("email2").value
                    val firstnickname = snapshot.children.first().child("nickname1").value
                    val secondenickname = snapshot.children.first().child("nickname2").value
                    val emil1CheckCount = snapshot.children.first().child("email1MessageCheck").value
                    val emil2CheckCount = snapshot.children.first().child("email2MessageCheck").value

                    if(firstemail.toString() == auth.currentUser?.email.toString()){
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val lastChatTime = snapshot.children.last().key?.toLong()
                                val lastMessage = snapshot.children.last().child("message").value.toString()
                                val notCheckChatCount : Int = totalChatCount - emil2CheckCount.toString().toInt()-1

                                val chatRoomProfile = ChatRoomData(
                                    secondeemail.toString(),
                                    secondenickname.toString(),
                                    lastMessage,
                                    lastChatTime,
                                    notCheckChatCount
                                )
                                chatRoomData.add(chatRoomProfile)//
                                chatProfileArray()
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                    }else{
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val lastChatTime = snapshot.children.last().key?.toLong()
                                val lastMessage = snapshot.children.last().child("message").value.toString()
                                val notCheckChatCount : Int = totalChatCount - emil1CheckCount.toString().toInt() - 1

                                val chatRoomProfile = ChatRoomData(
                                    firstemail.toString(),
                                    firstnickname.toString(),
                                    lastMessage,
                                    lastChatTime,
                                    notCheckChatCount
                                )

                                chatRoomData.add(chatRoomProfile)//
                                chatProfileArray()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun chatProfileArray(){
        val sortedList = chatRoomData.sortedByDescending { it.time }
        val chatProfile = mutableListOf<String?>()

        for (i in 0..sortedList.size-1){
            for (j in 0..friendsProfileList.value!!.size-1)
            {
                if(sortedList[i].nickname==friendsProfileList.value!![j].nickname){
                    chatProfile.add(friendsProfileList.value!![j].imgURL)
                }else{
                    true
                }
            }
        }
        chatProfileArray.value = chatProfile
        chatRoomProfileArray.value = sortedList.toMutableList()

    }

    fun fnStartFragmentScheduleSet(){
        scheduleCalendarFragmentStart.value = true
        scheduleCalendarFragmentStart.value = false
    }
    fun fnScheduleAm(){
        scheduleAmPmSet.value = true
    }
    fun fnSchedulePm(){
        scheduleAmPmSet.value = false
    }
    fun fnStartScheduleMapActivity(){
        scheduleMapActivityStart.value = true
    }
    fun fnEndMapActivity(){
        mapActivityEnd.value = true
    }

    fun fnScheduleDateSet(year: Int,month:Int,day:Int){
        var monthString = "00"
        var dayString = "00"

        if(month<10){
            monthString = "0"+month.toString()
        }else{
            monthString = month.toString()
        }
        if(day<10){
            dayString = "0"+day.toString()
        }else{
            dayString = day.toString()
        }

        scheduleDate.value = year.toString()+"년 "+monthString+"월 "+dayString+"일"
        scheduleYYYYMMDD.value = year.toString()+monthString+dayString
    }

    fun fnSearchKewordList(searchResult: KeyWordResponse?){
        val searchList : MutableList<PlaceItem> = mutableListOf()
        for (document in searchResult!!.items) {
            searchList.add(document)
        }
        searchKewordList.value = searchList
    }

    fun fnSelectKewordTabDataSet(mapPoint: LatLng, name: String, roadAddress:String, address:String){
        searchCoordinate.value = mapPoint
        kewordName.value = name
        selectAddress.value = address
        selectRoadAddress.value = roadAddress
        showSelectMarker.value = true
        showKeywordList.value = false
        searchKewordAndAddress.value = ""
        showSelectKeywordTab.value = true
    }

    fun fnDttmSet():String{
        var HHString :String="00"
        var MMString :String="00"
        var HHInt:Int = 0

        if (scheduleAmPmSet.value == true){
            if(scheduleHH.value == ""){
                HHInt = 0
            }else{
                HHInt = scheduleHH.value!!.toInt()
            }
        }else{
            if(scheduleHH.value == ""){
                HHInt = 0
            }else{
                HHInt = scheduleHH.value!!.toInt()+12
            }
        }

        if (HHInt<10){
            HHString = "0"+HHInt.toString()
        }else{
            HHString = HHInt.toString()
        }

        if(scheduleMM.value ==""){
            MMString = "00"
        }else{
            if (scheduleMM.value!!.toInt()<10){
                MMString = "0"+scheduleMM.value!!.toInt().toString()
            }else{
                MMString = scheduleMM.value!!
            }
        }

        val Dttm :String= scheduleYYYYMMDD.value!! + HHString + MMString
        return Dttm
    }

    fun fnTimeSet(hh:String,mm:String):String{//mm이 00으로 들어와서 min이 000이됨
        var hour : String = ""
        var min : String = ""
        var time : String = ""
        if(hh == "" && mm != ""){
            hour = "00"
            if(mm.toInt()<10){
                min = "0" + mm
            }else{
                min = mm
            }
        }else if(hh != "" && mm == ""){
            if(hh.toInt()<10){
                hour = "0" + hh.toInt().toString()
            }else{
                hour = hh
            }
            min = "00"
        }else if(hh != "" && mm != ""){
            if(hh.toInt()<10){
                hour = "0" + hh.toInt().toString()
            }else{
                hour = hh
            }
            if(mm.toInt()<10){
                min = "0" + mm.toInt().toString()//예외 필요
            }else{
                min = mm
            }
        }

        time = hour + min
        return time
    }

    fun fnTimeSplit(time : Int):String{
        var HM :String = ""
        val min = time.div(60)
        val quotient = min.div(60)
        val remainder = min.rem(60)
        HM = quotient.toString() + "시간" + remainder + "분"

        return  HM
    }

    fun fnSearchKeyword(keyword: String){
        val retrofit = Retrofit.Builder()
            .baseUrl(KEWORD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val naverapi = retrofit.create(NaverKeWord::class.java)
        val call = naverapi.getKeword(KEWORD_CLIENT_ID, KEWORD_API_KEY,keyword,5)

        call.enqueue(object: Callback<KeyWordResponse>{
            override fun onResponse(call: Call<KeyWordResponse>, response: Response<KeyWordResponse>) {
                if (response.isSuccessful){
                    fnSearchKewordList(response.body())
                    showKeywordList.value = true
                }else{
                    Log.w("NaverKewordAPI", "호출 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<KeyWordResponse>, t: Throwable) {
                Log.w("NaverKewordAPI", "통신 실패: ${t.message}")
            }
        })
    }

    fun fnReverseGeoAddressSet(body: ReverseGeocodingResponse?){
        var address : String = ""
        if(body!!.results[0] != null){
            if (body.results[0].land.number2==""){
                address = body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1
            }else{
                address = body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1+"-"+body.results[0].land.number2
            }
        }else{

        }

        selectAddress.value = address
    }

    fun fnReverseGeoRoadAddressSet(body: ReverseGeocodingResponse?){
        var roadAddress : String = ""

        if(body!!.results.size == 1){

        }else if(body!!.results.size == 2){
            if(body!!.results[1] != null){
                roadAddress = body.results[1].region.area1.name+body.results[1].region.area2.name+body.results[1].land.name+body.results[1].land.number1
            }
        }

        selectRoadAddress.value = roadAddress
    }

    fun fnReverseGeo(coords:String){
        val retrofit = Retrofit.Builder()
            .baseUrl(NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val naverapi = retrofit.create(NaverReverseGeocode::class.java)
        val call = naverapi.getReverseGeocode(NAVER_CLIENT_ID, NAVER_API_KEY,coords,"json","addr,roadaddr")

        call.enqueue(object: Callback<ReverseGeocodingResponse>{
            override fun onResponse(call: Call<ReverseGeocodingResponse>, response: Response<ReverseGeocodingResponse>) {

                if (response.isSuccessful) {

                    fnReverseGeoAddressSet(response.body())
                    fnReverseGeoRoadAddressSet(response.body())

                    kewordName.value =""//addition0.value값이 안들어옴 나중에 확인 해보자
                    showSelectKeywordTab.value = true
                }else{
                    Log.w("NaverReverseGeoAPI", "호출 실패: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<ReverseGeocodingResponse>, t: Throwable) {
                Log.w("NaverReverseGeoAPI", "통신 실패: ${t.message}")
            }
        })
    }

    fun fnStartingPointSet(){
        val retrofit = Retrofit.Builder()
            .baseUrl(NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val NaverGeo = retrofit.create(NaverGeocode::class.java)
        var splitAddress = profileAddress.value.toString().split(",")
        
        val call = NaverGeo.getGeocode(NAVER_CLIENT_ID, NAVER_API_KEY,splitAddress[1])

        call.enqueue(object : Callback<GeocodingRespone> {
            override fun onResponse(call: Call<GeocodingRespone>, response: Response<GeocodingRespone>) {
                if (response.isSuccessful) {//주소가 검색이안됨 도로명이라 그런가? 아니면 진짜 주소가 이상한가? 받아온 리스트 0 인것도 예외처리 해야할듯 다른곳도 확인
                    if (response.body()!!.addresses.size == 0) {
                        Log.w("NaverGeoAPI", "리스트 0: ${response.code()}")
                    }else{
                        startX.value = response.body()!!.addresses[0].x
                        startY.value = response.body()!!.addresses[0].y
                    }
                } else {
                    Log.w("NaverGeoAPI", "호출 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GeocodingRespone>, t: Throwable) {
                Log.w("NaverGeoAPI", "통신 실패: ${t.message}")
            }
        })
    }

    fun fnPublicTransportTimeSet(){
        if(scheduleAmPmSet.value == null){
            publicTransportCheck.value = 1
        }else if(fnTimeSet(scheduleHH.value!!, scheduleMM.value!!) == ""){
            publicTransportCheck.value = 2
        }else if(meetingPlaceAddress.value==""){
            publicTransportCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapTransitApiService = retrofit.create(TMapPublicTransportRoute::class.java)
            val startEndPoint = TransitRouteRequest(
                startX.value!!,
                startY.value!!,
                endX.value!!,
                endY.value!!,
                fnDttmSet())

            val call = TMapTransitApiService.getPublicTransportRoute(TMAP_API_KEY,startEndPoint)

            call.enqueue(object : Callback<PublicTransportRouteResponse> {
                override fun onResponse(call: Call<PublicTransportRouteResponse>, response: Response<PublicTransportRouteResponse>) {
                    if (response.isSuccessful) {

                        if(response.body()!!.metaData == null){
                            publicTransportCheck.value = 5

                        }else{
                            publicTransportTime.value = fnTimeSplit(response.body()!!.metaData.plan.itineraries[0].totalTime)
                            transportTime.value = response.body()!!.metaData.plan.itineraries[0].totalTime
                            selectTransport.value = 1
                        }

                    } else {
                        Log.w("TMapTratnportAPI", "호출 실패: ${response.code()}")
                        publicTransportCheck.value = 4
                    }
                }

                override fun onFailure(call: Call<PublicTransportRouteResponse>, t: Throwable) {
                    Log.w("TMapTratnportAPI", "통신 실패: ${t.message}")
                    publicTransportCheck.value = 4
                }
            })
        }
    }

    fun fnCarTimeSet(){
        if(scheduleAmPmSet.value == null){
            carCheck.value = 1
        }else if(fnTimeSet(scheduleHH.value!!, scheduleMM.value!!) == ""){
            publicTransportCheck.value = 2
        }else if(meetingPlaceAddress.value==""){
            carCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapCarApiService = retrofit.create(TMapCarRoute::class.java)
            val startEndPoint = CarRouteRequest(startX.value!!.toDouble(),
                startY.value!!.toDouble(),
                endX.value!!.toDouble(),
                endY.value!!.toDouble(),
                fnDttmSet()+"00")//end좌표가 비었음

            val call = TMapCarApiService.getCarRoute(TMAP_API_KEY,"1","CarRouteResponse",startEndPoint)

            call.enqueue(object : Callback<CarRouteResponse>{
                override fun onResponse(call: Call<CarRouteResponse>, response: Response<CarRouteResponse>) {
                    if (response.isSuccessful) {
                        //walkTime.value = fnTimeSplit(response.body()!!.features[0].properties.totalTime)
                        carTime.value = fnTimeSplit(response.body()!!.features[0].properties.totalTime)
                        transportTime.value = response.body()!!.features[0].properties.totalTime
                        selectTransport.value = 2
                    } else {
                        Log.w("TMapCarAPI", "호출 실패: ${response.code()}")
                        carCheck.value = 4
                    }
                }

                override fun onFailure(call: Call<CarRouteResponse>, t: Throwable) {
                    Log.w("TMapCarAPI", "통신 실패: ${t.message}")
                    carCheck.value = 4
                }
            })
        }
    }

    fun fnWalkingTimeSet(){
        if(scheduleAmPmSet.value == null){
            walkCheck.value = 1
        }else if(fnTimeSet(scheduleHH.value!!, scheduleMM.value!!) == ""){
            publicTransportCheck.value = 2
        }else if(meetingPlaceAddress.value==""){
            walkCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapWalkApiService = retrofit.create(TMapWalkRoute::class.java)
            val startEndPoint = WalkRouteRequest(startX.value!!.toDouble(),
                startY.value!!.toDouble(),
                endX.value!!.toDouble(),
                endY.value!!.toDouble(),
                "출발점",
                "도착점")
            val call = TMapWalkApiService.getWalkRoute(TMAP_API_KEY,"1","WalkRouteResponse",startEndPoint)

            call.enqueue(object : Callback<WalkRouteResponse>{
                override fun onResponse(call: Call<WalkRouteResponse>, response: Response<WalkRouteResponse>) {
                    if (response.isSuccessful) {
                        walkTime.value = fnTimeSplit(response.body()!!.features[0].properties.totalTime)
                        transportTime.value = response.body()!!.features[0].properties.totalTime
                        selectTransport.value = 3
                    } else {
                        Log.w("TMapWalkingAPI", "호출 실패: ${response.code()}")
                        walkCheck.value = 4
                    }
                }

                override fun onFailure(call: Call<WalkRouteResponse>, t: Throwable) {
                    Log.w("TMapWalkingAPI", "통신 실패: ${t.message}")
                    walkCheck.value = 4
                }
            })
        }
    }

    var transportTime : MutableLiveData<Int> = MutableLiveData()
    var appointmentRequestSuccess : MutableLiveData<Boolean> = MutableLiveData()

    fun fnAlarmYYYYMMDDhhmm(alarmTime: AlarmTime):String{
        var alarmYYYYMMDDhhmm : String = ""

        var YYYY = alarmTime.YYYY.toString()
        var MM = alarmTime.MM.toString()
        var DD = alarmTime.DD.toString()
        var hh = alarmTime.hh.toString()
        var mm = alarmTime.mm.toString()

        if(alarmTime.MM < 10){
            MM = "0" + MM
        }
        if(alarmTime.DD < 10){
            DD = "0" + DD
        }
        if(alarmTime.hh < 10){
            hh = "0" + hh
        }
        if(alarmTime.mm < 10){
            mm = "0" + mm
        }

        alarmYYYYMMDDhhmm = YYYY + MM + DD + hh + mm

        return alarmYYYYMMDDhhmm
    }

    var meetingTimeOver : MutableLiveData<Boolean> = MutableLiveData()

    fun fnAppointmentRequest(){
        val userEmail = auth.currentUser!!.email!!
        val userEmailReplace = userEmail.replace(".","_")
        val friendEmail = selectFriendProfile.value!!.email!!
        val friendEmailReplace = friendEmail.replace(".","_")
        val childName = userEmailReplace+"||"+friendEmailReplace
        val alarmTime = fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
        val alarmTimeSet = fnAlarmTimeSet()


        val currentTime = fnGetCurrentTimeString().substring(0,12)

        val alarmYYYYMMDDhhmm = fnAlarmYYYYMMDDhhmm(alarmTimeSet)

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            meetingTimeOver.value = true
            meetingTimeOver.value = false
        }else{

            val dataMap = mapOf(
                "email1" to userEmail,
                "email2" to friendEmail,
                "nickname1" to profileNickname.value,
                "nickname2" to selectFriendProfile.value!!.nickname,
                "meetingPlace" to meetingPlaceAddress.value,
                "meetingPlaceName" to kewordName.value,
                "meetingplaceX" to endX.value,
                "meetingplaceY" to endY.value,
                "meetingTime" to fnDttmSet(),
                "email1Status" to "consent",
                "email2Status" to "wait",
                "email1ProfileImgURl" to profileImgURL.value,
                "email2ProfileImgURl" to selectFriendProfile.value!!.imgURL,
                "email1Transport" to selectTransport.value,
                "email2Transport" to "",
                "email1Alarm" to alarmTime,
                "email1StartX" to startX.value,
                "email1StartY" to startY.value,
                "email2StartX" to "",
                "email2StartY" to "",
                "email1TransportTime" to transportTime.value,
                "email2TransportTime" to "",
                "email1StartCheck" to "not",
                "email2StartCheck" to "not",
                )

            val reference = database.getReference("appointment").child(childName)
            reference.setValue(dataMap).addOnSuccessListener{
                scheduleEmailPath.value = childName
                appointmentRequestSuccess.value = true
                appointmentRequestSuccess.value = false

                fnScheduleSetDataReset()
            }
        }
    }

    fun fnScheduleSetDataReset(){
        scheduleHH.value = ""
        scheduleMM.value = ""
        scheduleYYYYMMDD.value = ""
        scheduleAmPmSet.value = true
        scheduleAlarmHH.value = ""
        scheduleAlarmMM.value = ""
        selectTransport.value = 0
        transportTime.value = 0
        meetingPlaceAddress.value = ""
        endX.value = ""
        endY.value = ""
        selectScheduleNickname.value = ""
        scheduleEmailPath.value = ""
        publicTransportTime.value = ""
        carTime.value = ""
        walkTime.value = ""
    }

    fun fnFRDPathSplit(emailPath:String):Int{
        val email = auth.currentUser?.email.toString()
        val emailPathReplace = emailPath?.replace("_",".")
        var splitArray = emailPathReplace?.split("||")
        if(email == splitArray!![0]){
            return 1
        }else if(email == splitArray[1]){
            return 2
        }
        return 0
    }

    fun fnScheduleListData(){
        val scheduleList = mutableListOf<ScheduleSet>()
        val scheduleAlarmList = mutableListOf<ScheduleSet>()

        val reference = database.getReference("appointment")


        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {

                } else {
                    scheduleDataList.value = scheduleList
                    scheduleAlarmDataList.value = scheduleAlarmList
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        reference.addChildEventListener(object : ChildEventListener {//여기서 거절도 리스트에 넣을꺼임
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val emailPath = snapshot.key
                if(fnFRDPathSplit(emailPath!!) == 1){
                    val scheduleData = ScheduleSet(
                        emailPath,
                        snapshot.child("email2").value.toString(),
                        snapshot.child("nickname2").value.toString(),
                        snapshot.child("email2ProfileImgURl").value.toString(),
                        snapshot.child("meetingTime").value.toString(),
                        snapshot.child("meetingPlace").value.toString(),
                        snapshot.child("meetingPlaceName").value.toString(),
                        snapshot.child("email2Status").value.toString(),
                        snapshot.child("meetingplaceX").value.toString(),
                        snapshot.child("meetingplaceY").value.toString(),
                        snapshot.child("email2Alarm").value.toString(),
                        snapshot.child("email2Transport").value.toString(),
                        snapshot.child("email2StartX").value.toString(),
                        snapshot.child("email2StartY").value.toString(),
                        snapshot.child("email2TransportTime").value.toString(),
                        snapshot.child("email1Transport").value.toString(),
                        snapshot.child("email1Alarm").value.toString(),
                    )

                    if(snapshot.child("email1Status").value.toString() == "consent"){
                        scheduleList.add(scheduleData)
                        scheduleDataList.value = scheduleList//상대가 어떤 상태든 리스트에서 다시 확인해야함
                        scheduleAlarmDataList.value = scheduleAlarmList
                    }else if(snapshot.child("email1Status").value.toString() == "wait"){
                        scheduleAlarmList.add(scheduleData)
                        scheduleAlarmDataList.value = scheduleAlarmList
                        scheduleAlarmCheck.value = true
                    }

                }else if(fnFRDPathSplit(emailPath!!) == 2){
                    val scheduleData = ScheduleSet(
                        emailPath,
                        snapshot.child("email1").value.toString(),
                        snapshot.child("nickname1").value.toString(),
                        snapshot.child("email1ProfileImgURl").value.toString(),
                        snapshot.child("meetingTime").value.toString(),
                        snapshot.child("meetingPlace").value.toString(),
                        snapshot.child("meetingPlaceName").value.toString(),
                        snapshot.child("email1Status").value.toString(),
                        snapshot.child("meetingplaceX").value.toString(),
                        snapshot.child("meetingplaceY").value.toString(),
                        snapshot.child("email1Alarm").value.toString(),
                        snapshot.child("email1Transport").value.toString(),
                        snapshot.child("email1StartX").value.toString(),
                        snapshot.child("email1StartY").value.toString(),
                        snapshot.child("email1TransportTime").value.toString(),
                        snapshot.child("email2Transport").value.toString(),
                        snapshot.child("email2Alarm").value.toString(),
                    )
                    if(snapshot.child("email2Status").value.toString() == "consent"){
                        scheduleList.add(scheduleData)
                        scheduleDataList.value = scheduleList//상대가 어떤 상태든 리스트에서 다시 확인해야함
                        scheduleAlarmDataList.value = scheduleAlarmList
                    }else if(snapshot.child("email2Status").value.toString() == "wait"){
                        scheduleAlarmList.add(scheduleData)
                        scheduleAlarmDataList.value = scheduleAlarmList
                        scheduleAlarmCheck.value = true
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

    fun fnScheduleAcceptDataSet(position: Int,address: String?){
        val notSplitTime = scheduleAlarmDataList.value!![position].time
        val meetingAddress = scheduleAlarmDataList.value!![position].meetingPlaceAddress
        val endpointX = scheduleAlarmDataList.value!![position].endX
        val endpointY = scheduleAlarmDataList.value!![position].endY
        val emailPath = scheduleAlarmDataList.value!![position].scheduleName


        fnScheduleTimeSplit(notSplitTime)
        profileAddress.value = address
        meetingPlaceAddress.value = meetingAddress
        endX.value = endpointX
        endY.value = endpointY
        scheduleEmailPath.value = emailPath
        selectScheduleNickname.value = scheduleAlarmDataList.value!![position].nickname//내 닉네임 아님?

    }

    fun fnSchedulEditDataSet(position: Int){
        val notSplitTime = scheduleDataList.value!![position].time
        val meetingAddress = scheduleDataList.value!![position].meetingPlaceAddress
        val endpointX = scheduleDataList.value!![position].endX
        val endpointY = scheduleDataList.value!![position].endY
        val emailPath = scheduleDataList.value!![position].scheduleName
        val transport = scheduleDataList.value!![position].myTransport
        val alarm = scheduleDataList.value!![position].myAlarmTime

        fnScheduleTimeSplit(notSplitTime)//문제있음//고친건가?
        meetingPlaceAddress.value = meetingAddress
        endX.value = endpointX
        endY.value = endpointY
        scheduleEmailPath.value = emailPath
        selectTransport.value = transport.toInt()
        selectScheduleNickname.value = scheduleDataList.value!![position].nickname
        scheduleAlarmHH.value = alarm.substring(0,2).toInt().toString()
        scheduleAlarmMM.value = alarm.substring(2,4).toInt().toString()


        when(selectTransport.value){
            1 -> fnPublicTransportTimeSet()
            2 -> fnCarTimeSet()
            3 -> fnWalkingTimeSet()
        }
    }

    fun fnScheduleTimeSplit(date:String){
        val YYYYMMDD = date.substring(0,8)
        val YYYY = date.substring(0,4)
        val MM = date.substring(4,6)
        val DD = date.substring(6,8)
        val hh = date.substring(8,10)
        val mm = date.substring(10,12)

        val date = YYYY+"년 "+MM+"월 "+DD+"일"

        if(hh.toInt()<12){
            scheduleAmPmSet.value = true
            scheduleHH.value = hh

        }else{
            scheduleAmPmSet.value = false
            val pmhh = hh.toInt() - 12
            scheduleHH.value = pmhh.toString()
        }

        scheduleYYYYMMDD.value = YYYYMMDD
        scheduleDate.value = date
        scheduleMM.value = mm
    }

    fun fnScheduleAccept(){
        val currentTime = fnGetCurrentTimeString().substring(0,12)

        val alarmYYYYMMDDhhmm = fnAlarmYYYYMMDDhhmm(fnAlarmTimeSet())

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            meetingTimeOver.value = true
            meetingTimeOver.value = false
        }else{
            val emailPath = scheduleEmailPath.value
            val alarmTime = fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val reference = database.getReference("appointment").child(emailPath!!)
            if(fnFRDPathSplit(emailPath!!) == 1){
                reference.child("email1Alarm").setValue(alarmTime)
                reference.child("email1Transport").setValue(selectTransport.value)
                reference.child("email1StartX").setValue(startX.value)
                reference.child("email1StartY").setValue(startY.value)
                reference.child("email1TransportTime").setValue(transportTime.value)
                reference.child("email1Status").setValue("consent").addOnSuccessListener {
                    //scheduleEmailPath.value = emailPath!!
                    scheduleAcceptOK.value = true
                    scheduleAcceptOK.value = false
                    fnScheduleListData()
                }
            }else if(fnFRDPathSplit(emailPath!!) == 2){
                reference.child("email2Alarm").setValue(alarmTime)
                reference.child("email2Transport").setValue(selectTransport.value)
                reference.child("email2StartX").setValue(startX.value)
                reference.child("email2StartY").setValue(startY.value)
                reference.child("email2TransportTime").setValue(transportTime.value)
                reference.child("email2Status").setValue("consent").addOnSuccessListener {
                    scheduleAcceptOK.value = true
                    scheduleAcceptOK.value = false
                    fnScheduleListData()
                }
            }
        }
    }

    fun fnScheduleEditSuccess(){//asd
        val currentTime = fnGetCurrentTimeString().substring(0,12)

        val alarmYYYYMMDDhhmm = fnAlarmYYYYMMDDhhmm(fnAlarmTimeSet())

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            meetingTimeOver.value = true
            meetingTimeOver.value = false
        }else{
            val emailPath = scheduleEmailPath.value
            val alarmTime = fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val reference = database.getReference("appointment").child(emailPath!!)
            if(fnFRDPathSplit(emailPath!!) == 1){
                reference.child("email1Alarm").setValue(alarmTime)
                reference.child("email1StartX").setValue(startX.value)
                reference.child("email1StartY").setValue(startY.value)
                reference.child("email1TransportTime").setValue(transportTime.value)
                reference.child("email1Transport").setValue(selectTransport.value).addOnSuccessListener {
                    scheduleEditSuccess.value = true
                    scheduleEditSuccess.value = false
                    fnScheduleSetDataReset()
                }

            }else if(fnFRDPathSplit(emailPath!!) == 2){
                reference.child("email2Alarm").setValue(alarmTime)
                reference.child("email2StartX").setValue(startX.value)
                reference.child("email2StartY").setValue(startY.value)
                reference.child("email2TransportTime").setValue(transportTime.value)
                reference.child("email2Transport").setValue(selectTransport.value)
                scheduleEditSuccess.value = true
                scheduleEditSuccess.value = false
                fnScheduleSetDataReset()
            }
        }
    }

    fun fnAlarmTimeSet():AlarmTime{
        var alarmTime : AlarmTime

        var YYYY : Int = scheduleYYYYMMDD.value!!.substring(0,4).toInt()
        var MM : Int = scheduleYYYYMMDD.value!!.substring(4,6).toInt()
        var DD : Int = scheduleYYYYMMDD.value!!.substring(6,8).toInt()
        var hh : Int = 0
        var mm : Int = 0
        val transportMin = transportTime.value!!.div(60)
        var alarmhh : Int = 0
        var alarmmm : Int = 0

        if(scheduleHH.value != ""){
            hh = scheduleHH.value!!.toInt()
        }
        if(scheduleMM.value != ""){
            mm = scheduleMM.value!!.toInt()
        }

        if (scheduleAlarmHH.value!! != ""){
            alarmhh = scheduleAlarmHH.value!!.toInt()
        }

        if (scheduleAlarmMM.value!! != ""){
            alarmmm = scheduleAlarmMM.value!!.toInt()
        }

        if(scheduleAmPmSet.value == true){
            hh = scheduleHH.value!!.toInt()
        }else{
            hh = scheduleHH.value!!.toInt() + 12
        }

        mm = mm - transportMin.rem(60) - alarmmm
        if(mm < 0){
            mm = mm + 60
            hh = hh - 1
            if(mm<0){
                mm = mm + 60
                hh = hh - 1
            }
        }

        hh = hh - transportMin.div(60) - alarmhh

        if(hh < 0){
            hh = hh + 24
            DD = DD -1
        }

        if(DD < 1){
            MM = MM - 1
            when(MM){
                0,1,3,5,7,8,10,12 -> DD = DD + 31
                4,6,9,11 -> DD = DD + 30
                2->{
                    if(YYYY.rem(4)==0){
                        DD = DD + 29
                    }else{
                        DD = DD + 28
                    }
                }
            }
        }

        if(MM < 1){
            MM = MM + 12
            YYYY = YYYY - 1
        }

        alarmTime = AlarmTime(YYYY,MM,DD, hh, mm)

        return alarmTime
    }

    fun fnCheckStartAlarmTimeSet():AlarmTime{
        var alarmTime : AlarmTime

        var YYYY : Int = scheduleYYYYMMDD.value!!.substring(0,4).toInt()
        var MM : Int = scheduleYYYYMMDD.value!!.substring(4,6).toInt()
        var DD : Int = scheduleYYYYMMDD.value!!.substring(6,8).toInt()
        var hh : Int = 0
        var mm : Int = 0
        //var mm : Int = scheduleMM.value!!.toInt()
        val transportMin = transportTime.value!!.div(60)

        if(scheduleHH.value != ""){
            if(scheduleAmPmSet.value == true){
                hh = scheduleHH.value!!.toInt()
            }else{
                hh = scheduleHH.value!!.toInt() + 12
            }
        }
        if(scheduleMM.value != ""){
            mm = scheduleMM.value!!.toInt()
        }

        mm = mm - transportMin.rem(60)
        if(mm < 0){
            mm = mm + 60
            hh = hh - 1
        }

        hh = hh - transportMin.div(60)

        if(hh < 0){
            hh = hh + 24
            DD = DD -1
        }

        if(DD < 1){
            MM = MM - 1
            when(MM){
                0,1,3,5,7,8,10,12 -> DD = DD + 31
                4,6,9,11 -> DD = DD + 30
                2->{
                    if(YYYY.rem(4)==0){
                        DD = DD + 29
                    }else{
                        DD = DD + 28
                    }
                }
            }
        }

        if(MM < 1){
            MM = MM + 12
            YYYY = YYYY - 1
        }

        alarmTime = AlarmTime(YYYY,MM,DD, hh, mm)

        return alarmTime
    }

    fun fnScheduleListDelete(){
        val emailPath : String = scheduleDataList.value!![selectPosition.value!!].scheduleName

        val reference = database.getReference("appointment").child(emailPath)

        reference.removeValue()
        fnScheduleListData()


        scheduleListDelete.value = true
    }

    fun fnScheduleListDeleteCancel(){
        scheduleListDelete.value = false
    }

    fun fnDeleteTextSet(position: Int){
        scheduleDeleteText.value = scheduleDataList.value!![position].nickname + "님과의 약속을 제거하시겠습니까?"
    }

    var scheduleRefuse : MutableLiveData<Boolean> = MutableLiveData()

    fun fnScheduleRefuse(){

        val emailPath = scheduleEmailPath.value
        val reference = database.getReference("appointment").child(emailPath!!)
        if(fnFRDPathSplit(emailPath!!) == 1){
            reference.child("email1Status").setValue("refuse").addOnSuccessListener {
                scheduleRefuse.value = true
                scheduleRefuse.value = false
                fnScheduleListData()
            }
        }else if(fnFRDPathSplit(emailPath!!) == 2){
            reference.child("email2Status").setValue("refuse").addOnSuccessListener {
                scheduleRefuse.value = true
                scheduleRefuse.value = false
                fnScheduleListData()
            }
        }

    }


    fun fnScheduleRefuseOk(){

        val emailPath : String = scheduleDataList.value!![selectPosition.value!!].scheduleName

        val reference = database.getReference("appointment").child(emailPath)

        reference.removeValue()
        fnScheduleListData()

        scheduleRefuseOkView.value = true
        scheduleRefuseOkView.value = false
    }
}





