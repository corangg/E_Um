package com.example.appointment.repository

import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class ProfileRepository (private val utils: Utils) {
    private val userEmail = auth.currentUser?.email

    suspend fun getPrivacyData(): Map<String, Any>? {
        val docRef = firestore.collection("Privacy").document(userEmail!!)
        return utils.readDataFirebase(docRef)
    }

    suspend fun getProfileData(): Map<String, Any>? {
        val docRef = firestore.collection("Profile").document(userEmail!!)
        return utils.readDataFirebase(docRef)
    }

    fun setProfileData(profileNickname: String?, profileStatusMessage: String?){
        val docRef = firestore.collection("Profile").document(userEmail!!)

        utils.updataDataFireBase(docRef,"nickname",profileNickname)
        utils.updataDataFireBase(docRef,"statusmessage",profileStatusMessage)
    }

    fun setPrivacyData(profileAddress:String?, profileNickname: String?){
        val splitAddress = profileAddress?.split(",")
        val docRef = firestore.collection("Privacy").document(userEmail!!)

        utils.updataDataFireBase(docRef,"zonecode",splitAddress!![0])
        utils.updataDataFireBase(docRef,"nickname",profileNickname)
        utils.updataDataFireBase(docRef,"address",splitAddress!![1])
    }

    fun setProfileImage(photoUri: Uri?){
        if(photoUri!=null){
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
            var storagePath = Utils.storage.reference.child("userProfile").child(userEmail!!).child("profileimg")

            val userEmail = auth.currentUser!!.email
            val docRef = firestore.collection("Profile").document(userEmail!!)

            storagePath.putFile(photoUri).continueWithTask{
                return@continueWithTask storagePath.downloadUrl
            }.addOnCompleteListener{downloadUrl->
                utils.updataDataFireBase(docRef,"imgURL",downloadUrl.result.toString())
                utils.updataDataFireBase(docRef,"timestamp",timestamp)
            }
        }
    }
}
