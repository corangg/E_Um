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
import com.example.appointment.model.ProfileDataModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
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

    init {
        //공유로 쓸거면 애매해 지는데 나중에 옮기는거 고려해야 할듯
        fnGetPrivacyData()
        fnGetProfileData()
        fnFriendRequestAlarm()
        fnFriendList()

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

    fun profileEditMode(uri: Uri?) {
        if(editProfileData.value == true){
            fnProfileImageUpdate(uri)
            fnProfileEdit()
            fnPrivacyEdit()
        }
        editProfileData.value = editProfileData.value?.not() ?: true
    }

    fun fnGetPrivacyData() {
        val userEmail = auth.currentUser?.email ?: ""
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
        val userEmail = auth.currentUser!!.email
        val docRef = firestore.collection("Profile").document(userEmail!!)

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
        val userEmail = auth.currentUser!!.email
        val docRef = firestore.collection("Profile").document(userEmail!!)

        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"statusmessage",profileStatusMessage.value)

    }

    fun fnPrivacyEdit(){
        val splitAddress = utils.splitString(profileAddress.value!!,",")
        val userEmail = auth.currentUser!!.email
        val docRef = firestore.collection("Privacy").document(userEmail!!)

        utils.updataDataFireBase(docRef,"zonecode",splitAddress[0])
        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"address",splitAddress[1])
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
        val userEmail = auth.currentUser?.email!!
        val emailReplace = userEmail.replace(".", "_")
        val reference = database.getReference(emailReplace).child("friendRequest")

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
    }//코루틴사용해야함

    fun fnFriendList(){
        viewModelScope.launch {
            val userEmail = auth.currentUser?.email ?: ""
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



}