package com.example.appointment.login

import android.app.Application
import android.icu.text.SimpleDateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.AddressDBHelper
import com.example.appointment.model.PrivacyDataModel
import com.example.appointment.model.ProfileDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class Signup_Viewmodel(application: Application) : AndroidViewModel(application){
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
    var addressName : MutableLiveData<String> = MutableLiveData("")

    var passwordCheck :MutableLiveData<Boolean> = MutableLiveData(false)
    var signupSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    var signupIdCheck :MutableLiveData<Boolean> = MutableLiveData(true)
    var searchAddress :MutableLiveData<Boolean> = MutableLiveData(false)
    var addressAdd :MutableLiveData<Boolean> = MutableLiveData(false)

    var auth = FirebaseAuth.getInstance()
    var firestore = FirebaseFirestore.getInstance()

    var context = getApplication<Application>().applicationContext

    var address: String =""

    fun signUp(){
        if(signUpPassword.value.toString()==signUpPasswordCheck.value.toString()){
            auth.createUserWithEmailAndPassword(signUpEmail.value.toString(),signUpPassword.value.toString()).addOnCompleteListener {
                if(it.isSuccessful){
                    val fulladdress = addressData.value + signUpDetailAddress.value
                    dbWrite("집",fulladdress)
                    savePrivacy()
                }else{
                    signupIdCheck.value =false
                }
            }
        }else{
            passwordCheck.value = true
        }
    }

    fun savePrivacy(){


        var profileModel = ProfileDataModel(
            auth.currentUser?.email!!,
            signUpNickName.value.toString(),
            "",
            signUpPhoneNumber.value.toString(),
            "")
        firestore.collection("Profile").document(signUpEmail.value.toString()).set(profileModel)

        var findIdModel = PrivacyDataModel(
            auth.currentUser?.email!!,
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

    fun addAddress(){
        address = addressData.value + signUpDetailAddress.value
        addressAdd.value = true
    }

    fun dbWrite(addressname:String?,address:String?){
        val db = AddressDBHelper(context,signUpEmail.value.toString()).writableDatabase
        db.execSQL("insert into ADDRESS_TB(name,address) values(?,?)", arrayOf(addressname,address))
        db.close()
    }



}