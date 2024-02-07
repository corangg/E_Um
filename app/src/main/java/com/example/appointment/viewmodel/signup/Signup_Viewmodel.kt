package com.example.appointment.viewmodel.signup

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.AddressDBHelper
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.model.PrivacyDataModel
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.viewmodel.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Signup_Viewmodel(application: Application) : BaseViewModel(application){
    var signUpEmail : MutableLiveData<String> = MutableLiveData("")
    var signUpPassword : MutableLiveData<String> = MutableLiveData("")
    var signUpPasswordCheck : MutableLiveData<String> = MutableLiveData("")
    var signUpName : MutableLiveData<String> = MutableLiveData("")
    var signUpNickName : MutableLiveData<String> = MutableLiveData("")
    var signUpPhoneNumber : MutableLiveData<String> = MutableLiveData("")
    var signUpZoneCode : MutableLiveData<String> = MutableLiveData("")
    var signUpAddress : MutableLiveData<String> = MutableLiveData("")
    var signUpDetailAddress : MutableLiveData<String> = MutableLiveData("")
    var addressData : MutableLiveData<String> = MutableLiveData("")
    val titleAddress : MutableLiveData<String> = MutableLiveData("")

    var passwordCheck :MutableLiveData<Unit> = MutableLiveData()
    var signupSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    var signupIdCheck :MutableLiveData<Boolean> = MutableLiveData(true)
    var searchAddress :MutableLiveData<Boolean> = MutableLiveData(false)

    //var context = getApplication<Application>().applicationContext

    fun signUp(){
        if(signUpPassword.value.toString()==signUpPasswordCheck.value.toString()){
            auth.createUserWithEmailAndPassword(signUpEmail.value.toString(),signUpPassword.value.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    titleAddress.value = addressData.value + signUpDetailAddress.value
                    savePrivacy()
                }else{
                    signupIdCheck.value =false
                }
            }
        }else{
            passwordCheck.value = Unit
        }
    }

    fun savePrivacy(){
        var profileModel = ProfileDataModel(
            userEmail,
            signUpNickName.value.toString(),
            "",
            signUpPhoneNumber.value.toString(),
            "")
        firestore.collection("Profile").document(signUpEmail.value.toString()).set(profileModel)

        var findIdModel = PrivacyDataModel(
            userEmail,
            signUpPassword.value.toString(),
            signUpName.value.toString(),
            signUpNickName.value.toString(),
            signUpPhoneNumber.value.toString(),
            signUpZoneCode.value.toString(),
            signUpAddress.value.toString()+signUpDetailAddress.value.toString())
        firestore.collection("Privacy").document(signUpEmail.value.toString()).set(findIdModel).addOnCompleteListener {
            if(it.isSuccessful){
                signupSuccess.value = true
                auth.currentUser?.sendEmailVerification()
            }
        }
    }

    fun searchAddress(){
        searchAddress.value = true
    }

    fun splitAddress(){
        var splitArray = addressData.value.toString().split(",")
        signUpZoneCode.value = splitArray[0]
        signUpAddress.value = splitArray[1]
    }

    fun fnHandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when(requestCode){
            RequestCode.ADDRESS_REQUEST_CODE->{
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    addressData.value = data?.extras?.getString("address")
                    splitAddress()
                }
            }
        }
    }
}