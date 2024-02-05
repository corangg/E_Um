package com.example.appointment.viewmodel.profile

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.FirebaseCertified

import com.example.appointment.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.data.Utils.Companion.storage
import com.example.appointment.model.ChatRoomData
import com.example.appointment.model.ProfileDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (/*val utils: Utils,*/application: Application) : AndroidViewModel(application) {

    val profileNickname: MutableLiveData<String> = MutableLiveData("")
    val profileStatusMessage: MutableLiveData<String> = MutableLiveData("")
    val profileName: MutableLiveData<String> = MutableLiveData("")
    val profilePhone: MutableLiveData<String> = MutableLiveData("")
    val profileEmail: MutableLiveData<String> = MutableLiveData("")
    val profilePassword: MutableLiveData<String> = MutableLiveData("")
    val profileAddress: MutableLiveData<String> = MutableLiveData("")
    val profileImgURL :MutableLiveData<String> = MutableLiveData("")

    val editProfileData :MutableLiveData<Boolean> = MutableLiveData(false)
    val openGallery :MutableLiveData<Boolean> = MutableLiveData(false)
    val nickNameEditActivityStart:MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordEdit :MutableLiveData<Boolean> = MutableLiveData(false)
    val addressEditActivityStart :MutableLiveData<Boolean> = MutableLiveData(false)
    val logOutSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    val imageUpload :MutableLiveData<Boolean> = MutableLiveData(false)


    private val utils = Utils()

    private val userEmail = auth.currentUser?.email ?: ""
    private val userEmailReplace = userEmail!!.replace(".", "_")

    init {
        //공유로 쓸거면 애매해 지는데 나중에 옮기는거 고려해야 할듯
        fnGetPrivacyData()
        fnGetProfileData()
        fnFriendRequestAlarm()
        fnFriendList()
        fnChatRoomList()
    }

    fun fnHandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when (requestCode) {
            RequestCode.NICKNAME_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        profileNickname.value = it.getStringExtra("nickname")
                        profileStatusMessage.value = it.getStringExtra("statusmessage")
                    }
                }
            }
            RequestCode.ADDRESS_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        profileAddress.value = it.getStringExtra("titleAddress")
                    }
                }
            }
            RequestCode.ACCEPT_FRIEND_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        fnFriendRequestAlarm()
                        fnFriendList()
                    }
                }
            }
            RequestCode.CHAT_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        fnChatRoomList()
                    }
                }

            }
        }
    }


    //profile
    fun fnProfileAddressEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnNickNameEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnPasswordEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }


    fun fnOpenGallery(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnLogout(){
        FirebaseCertified.firebaseAuth.signOut()
        FirebaseCertified.email = null
        logOutSuccess.value = true
    }

    fun fnProfileEditMode(uri: Uri?) {
        if(editProfileData.value == true){
            fnProfileImageUpdate(uri)
            fnProfileEdit()
            fnPrivacyEdit()
        }
        editProfileData.value = editProfileData.value?.not() ?: true
    }

    fun fnGetPrivacyData() {
        val docRef = firestore.collection("Privacy").document(userEmail)

        viewModelScope.launch {
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null){
                    profileEmail.value = dataMap?.get("email") as? String
                    profileName.value = dataMap?.get("name") as? String
                    profileNickname.value = dataMap?.get("nickname") as? String
                    profilePhone.value = dataMap?.get("phoneNumber") as? String
                    profilePassword.value = dataMap?.get("password") as? String
                    profileAddress.value = dataMap?.get("zonecode") as? String + "," + dataMap?.get("address") as? String
                }
            }catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend list", e)
            }
        }
    }

    fun fnGetProfileData(){
        val docRef = firestore.collection("Profile").document(userEmail)

        viewModelScope.launch {
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null){
                    profileImgURL.value = dataMap?.get("imgURL") as? String
                    profileStatusMessage.value = dataMap?.get("statusmessage") as? String
                    imageUpload.value = true
                }
            }catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend list", e)
            }
        }
    }

    fun fnProfileEdit(){
        val docRef = firestore.collection("Profile").document(userEmail)

        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"statusmessage",profileStatusMessage.value)

    }

    fun fnPrivacyEdit(){
        val splitAddress = profileAddress.value?.split(",")
        val userEmail = auth.currentUser!!.email
        val docRef = firestore.collection("Privacy").document(userEmail!!)

        utils.updataDataFireBase(docRef,"zonecode",splitAddress!![0])
        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"address",splitAddress!![1])
    }

    fun fnProfileImageUpdate(photoUri: Uri?){
        if(photoUri!=null){
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
            var storagePath = storage.reference.child("userProfile").child(auth.currentUser?.email.toString()).child("profileimg")

            val userEmail = auth.currentUser!!.email
            val docRef = firestore.collection("Profile").document(userEmail!!)


            storagePath.putFile(photoUri!!).continueWithTask{
                return@continueWithTask storagePath.downloadUrl
            }.addOnCompleteListener{downloadUrl->
                viewModelScope.launch {
                    utils.updataDataFireBase(docRef,"imgURL",downloadUrl.result.toString())
                    utils.updataDataFireBase(docRef,"timestamp",timestamp)
                }
            }
        }
    }


    //friend_fragment

    val friendRequestAlarmStatus : MutableLiveData<Boolean> = MutableLiveData()
    val friendsProfileList: MutableLiveData<MutableList<ProfileDataModel>> = MutableLiveData()
    val selectFriendProfile : MutableLiveData<ProfileDataModel> = MutableLiveData()

    val startFriendAddActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendAlarmActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendProfileFragment : MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnFriendRequestAlarm() {
        //val emailReplace = userEmail.replace(".", "_")
        val reference = database.getReference(userEmailReplace).child("friendRequest")

        reference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    friendRequestAlarmStatus.value = true

                } else {
                    friendRequestAlarmStatus.value = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fnFriendList(){
        viewModelScope.launch {
            val docRef = firestore.collection("Friend").document(userEmail)

            val friendEmails = fetchFriendList(docRef)
            val friendsProfileListReturn = fetchFriendProfiles(friendEmails)
            friendsProfileList.value = friendsProfileListReturn
        }
    }

    suspend fun fetchFriendList(docRef : DocumentReference): List<String> {
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

    suspend fun fetchFriendProfiles(friendEmails: List<String>): MutableList<ProfileDataModel> {
        val profileList = mutableListOf<ProfileDataModel>()

        for (friendEmail in friendEmails) {
            val docRef = firestore.collection("Profile").document(friendEmail)
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

    fun fnSelectFriendAddItem(){
        StartEvent(startFriendAddActivity)
    }

    fun fnSelectFriendAlarmItem(){
        StartEvent(startFriendAlarmActivity)
    }

    fun fnSelectFriend(position:Int){
        selectFriendProfile.value = friendsProfileList.value!![position]
        StartEvent(startFriendProfileFragment)
    }


    //friendProfile
    var chatCount : MutableLiveData<Int?> = MutableLiveData(0)
    var chatRoomName:MutableLiveData<String?> = MutableLiveData("")
    var startChatActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    var startScheduleCalendarFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    var friendDeleteSuccess:MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnChatStart(friendEmail: String?){
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val reference = database.getReference("chat")
        val chatName = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = utils.reverseString(chatName,"||")

        reference.child(chatName).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    chatCount.value = snapshot.childrenCount.toInt()
                    chatRoomName.value=chatName
                    StartEvent(startChatActivity)
                } else {
                    reference.child(reversedchatName).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                chatCount.value = snapshot.childrenCount.toInt()
                                chatRoomName.value = reversedchatName
                                StartEvent(startChatActivity)
                            } else {
                                val chatPath = reference.child(reversedchatName).child(utils.fnGetCurrentTimeString())
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
                                    StartEvent(startChatActivity)
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

    fun fnStartFragmentScheduleSet(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnFriendDelete(){
        val docRef = firestore.collection("Friend")
        val updates = HashMap<String, Any>()
        val friendEmail = selectFriendProfile.value!!.email
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val chatName : String = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = utils.reverseString(chatName,"||")
        val chatReference = database.getReference("chat")
        val appointmentReference = database.getReference("appointment")

        updates[selectFriendProfile.value!!.nickname] = FieldValue.delete()
        docRef.document(userEmail).update(updates)

        updates[profileNickname.value!!] = FieldValue.delete()
        docRef.document(friendEmail).update(updates)

        chatReference.child(chatName).removeValue().addOnSuccessListener {
            appointmentReference.child(chatName).removeValue()
        }
        chatReference.child(reversedchatName).removeValue().addOnSuccessListener {
            appointmentReference.child(reversedchatName).removeValue()
        }
        fnFriendList()
        StartEvent(friendDeleteSuccess)
    }


    //chatFragment
    val chatRoomProfileList:MutableLiveData<MutableList<ChatRoomData>> = MutableLiveData()
    var chatRoomData = mutableListOf<ChatRoomData>()
    val chatProfileList : MutableLiveData<MutableList<String?>> = MutableLiveData()
    val chatRoomFriendNickName: MutableLiveData<String> = MutableLiveData("")
    val chatFriendImg: MutableLiveData<String> = MutableLiveData("")

    fun fnChatRoomList(){
        val reference = database.getReference("chat")
        var chatRoomSize : Int = 0

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference){
                        val emailPath = it.key?: ""
                        val replaceEmailPath = emailPath.replace("_",".")
                        val splitArray = replaceEmailPath.split("||")
                        val totalChatCount = it.childrenCount.toInt()

                        if(chatRoomSize == 0){
                            chatRoomProfileList.value = mutableListOf()
                        }//원래는 밑이었는데 밑이면 아예 의미가 없을꺼 같은데? 이상하면 확인해 보자

                        if(userEmail == splitArray[0]){
                            chatProfile(emailPath,totalChatCount)
                            chatRoomSize = chatRoomSize + 1

                        }else if(userEmail == splitArray[1]){
                            chatProfile(emailPath,totalChatCount)
                            chatRoomSize = chatRoomSize + 1
                        }
                    }
                } else {
                    chatRoomProfileList.value = mutableListOf()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun chatProfile(emailPath:String?,totalChatCount:Int){
        val reference = database.getReference("chat").child(emailPath!!)
        chatRoomData = mutableListOf()

        /*var a:Int =  0
        utils.readDataFRDAddChildEventListener(reference){
            a = a+1

            if(a>totalChatCount){
                asf(reference,a)


                *//*val at = it.child("message").value.toString()


                at*//*
            }

            true
        }*/
        //채팅 오면 바로 리셋되게 하고 싶은데 어떻게 해야될지 고민임 폰이 하나라 확인이 어려움 쳇엑티 처럼 하면 되나?

        fnLastChatSet(reference,totalChatCount)
    }

    fun fnLastChatSet(reference:DatabaseReference,totalChatCount:Int){
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
                                val notCheckChatCount = totalChatCount - emil2CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,secondeemail,secondenickname)
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }else{
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val notCheckChatCount : Int = totalChatCount - emil1CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,firstemail,firstnickname)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun fnChatRoomDataAdd(snapshot: DataSnapshot, notCheckChatCount: Int, email: String, nickname: String){
        val lastChatTime = snapshot.children.last().key?.toLong()
        val lastMessage = snapshot.children.last().child("message").value.toString()

        val chatRoomProfile = ChatRoomData(
            email,
            nickname,
            lastMessage,
            lastChatTime,
            notCheckChatCount
        )
        chatRoomData.add(chatRoomProfile)
        fnChatProfileArray()
    }

    fun fnChatProfileArray(){
        val sortedList = chatRoomData.sortedByDescending { it.time }
        val chatProfile = mutableListOf<String?>()

        for (i in 0..sortedList.size-1){
            for (j in 0..friendsProfileList.value!!.size-1)
            {
                if(sortedList[i].nickname==friendsProfileList.value!![j].nickname){
                    chatProfile.add(friendsProfileList.value!![j].imgURL)
                }else{
                }
            }
        }
        chatProfileList.value = chatProfile
        chatRoomProfileList.value = sortedList.toMutableList()
    }

    fun fnSelectChat(position: Int){
        chatRoomFriendNickName.value = chatRoomProfileList.value!![position].nickname
        chatFriendImg.value = chatProfileList.value!![position]
        fnChatStart(chatRoomProfileList.value!![position].email)
    }

}