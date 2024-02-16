package com.example.appointment.viewmodel.signup

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.AddressDBHelper
import com.example.appointment.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.model.PrivacyDataModel
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.SignUpDataModel
import com.example.appointment.repository.SignUpRepository
import com.example.appointment.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Signup_Viewmodel @Inject constructor(
    application : Application,
    private val signUpRepository: SignUpRepository) : BaseViewModel(application){
    val signUpEmail : MutableLiveData<String> = MutableLiveData("")
    val signUpPassword : MutableLiveData<String> = MutableLiveData("")
    val signUpPasswordCheck : MutableLiveData<String> = MutableLiveData("")
    val signUpName : MutableLiveData<String> = MutableLiveData("")
    val signUpNickName : MutableLiveData<String> = MutableLiveData("")
    val signUpPhoneNumber : MutableLiveData<String> = MutableLiveData("")
    val signUpZoneCode : MutableLiveData<String> = signUpRepository.signUpZoneCode
    val signUpAddress : MutableLiveData<String> = signUpRepository.signUpAddress
    val signUpDetailAddress : MutableLiveData<String> = MutableLiveData("")
    val titleAddress : MutableLiveData<String> = MutableLiveData("")

    val passwordCheck :MutableLiveData<Unit> = MutableLiveData()
    val signupSuccess :MutableLiveData<Boolean> = signUpRepository.signupSuccess
    val signupIdCheck :MutableLiveData<Boolean> = MutableLiveData(false)
    val searchAddress :MutableLiveData<Boolean> = MutableLiveData(false)
    val writeAddressDB : MutableLiveData<Boolean> = MutableLiveData(false)

    var addressData : String = ""

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when(requestCode){
            RequestCode.ADDRESS_REQUEST_CODE->{
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    addressData = data?.extras?.getString("address")!!
                    signUpRepository.splitAddress(addressData)
                }
            }
        }
    }

    fun signUp(){
        if(signUpPassword.value!! ==signUpPasswordCheck.value!!){
            val profileDataModel = ProfileDataModel(
                userEmail,
                signUpNickName.value.toString(),
                "",
                signUpPhoneNumber.value.toString(),
                "")
            val privacyDataModel = PrivacyDataModel(
                userEmail,
                signUpPassword.value.toString(),
                signUpName.value.toString(),
                signUpNickName.value.toString(),
                signUpPhoneNumber.value.toString(),
                signUpZoneCode.value.toString(),
                signUpAddress.value.toString()+signUpDetailAddress.value.toString())
            val signUpData = SignUpDataModel(privacyDataModel,profileDataModel)

            viewModelScope.launch {
                if(signUpRepository.signUpTry(signUpEmail.value!!, signUpPassword.value!!, signUpData)){
                    titleAddress.value = addressData + signUpDetailAddress.value
                    StartEvent(writeAddressDB)
                }else{
                    StartEvent(signupIdCheck)
                }
            }
        }else{
            passwordCheck.value = Unit
        }
    }

    fun searchAddress(){
        StartEvent(searchAddress)
    }
}