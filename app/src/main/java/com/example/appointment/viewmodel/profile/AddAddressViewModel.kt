package com.example.appointment.viewmodel.profile

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.data.StartEvent
import com.example.appointment.data.RequestCode

class AddAddressViewModel(application: Application): AndroidViewModel(application) {
    val zoneCode : MutableLiveData<String> = MutableLiveData("")
    val address : MutableLiveData<String> = MutableLiveData("")
    val detailAddress : MutableLiveData<String> = MutableLiveData("")
    val addressName : MutableLiveData<String> = MutableLiveData("")

    val searchAddress :MutableLiveData<Boolean> = MutableLiveData(false)
    val editAddress :MutableLiveData<Boolean> = MutableLiveData(false)

    var addressData : String = ""

    fun searchAddress(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun editAddress(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun resultAddress(requestCode: Int, resultCode: Int, data: Intent?){
        when (requestCode) {
            RequestCode.ADDRESS_REQUEST_CODE -> {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    addressData = data?.extras?.getString("address")!!
                    val splitArray = addressData.split(",")
                    zoneCode.value = splitArray[0]
                    address.value = splitArray[1]
                }
            }
        }
    }


}