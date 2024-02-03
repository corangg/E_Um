package com.example.appointment.viewmodel.profile

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide.init
import com.example.appointment.FirebaseCertified

import com.example.appointment.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.data.Utils.Companion.storage
import com.example.appointment.ui.fragment.profile.Profile_Fragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

//@HiltViewModel@Inject constructorprivate val utils :Utils,
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

    val accountError: MutableLiveData<Int> = MutableLiveData(-1)

    private val utils = Utils()

    init {
        fnGetPrivacyData()
        fnGetProfileData()
    }

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

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
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
        }
    }

    fun fnGetPrivacyData() {
        val user = auth.currentUser

        if (user != null) {
            val userEmail = user.email
            val docRef = firestore.collection("Privacy").document(userEmail!!)

            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data

                    profileEmail.value = dataMap?.get("email") as? String
                    profileName.value = dataMap?.get("name") as? String
                    profileNickname.value = dataMap?.get("nickname") as? String
                    profilePhone.value = dataMap?.get("phoneNumber") as? String
                    profilePassword.value = dataMap?.get("password") as? String
                    profileAddress.value = dataMap?.get("zonecode") as? String + "," + dataMap?.get("address") as? String
                } else {
                    accountError.value = 0
                }
            }
        }
    }

    fun fnGetProfileData(){
        val user = auth.currentUser

        if (user != null) {
            val userEmail = user.email
            val docRef = firestore.collection("Profile").document(userEmail!!)

            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val dataMap = documentSnapshot.data

                    profileImgURL.value = dataMap?.get("imgURL") as? String
                    profileStatusMessage.value = dataMap?.get("statusmessage") as? String
                    imageUpload.value = true

                } else {
                    accountError.value = 1
                }
            }
        }
    }

    fun fnProfileEdit(){
        firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("nickname",profileNickname.value)
        firestore.collection("Profile").document(auth.currentUser?.email.toString()).update("statusmessage",profileStatusMessage.value)
    }

    fun fnPrivacyEdit(){
        val splitAddress = utils.splitString(profileAddress.value!!,",")

        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("zonecode",splitAddress[0])
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("nickname",profileNickname.value)
        firestore.collection("Privacy").document(auth.currentUser?.email.toString()).update("address",splitAddress[1])
    }

    fun fnProfileImageUpdate(photoUri: Uri?){
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
}