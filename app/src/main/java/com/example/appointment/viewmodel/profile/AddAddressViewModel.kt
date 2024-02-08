package com.example.appointment.viewmodel.profile

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.ui.activity.profile.AdressSearchActivity
import com.example.appointment.viewmodel.BaseViewModel

class AddAddressViewModel(application: Application): AndroidViewModel(application) {
    var zoneCode : MutableLiveData<String> = MutableLiveData("")
    var address : MutableLiveData<String> = MutableLiveData("")
    var detailAddress : MutableLiveData<String> = MutableLiveData("")
    var addressName : MutableLiveData<String> = MutableLiveData("")

    var addressData : MutableLiveData<String> = MutableLiveData("")
    var searchAddress :MutableLiveData<Boolean> = MutableLiveData(false)
    var editAddress :MutableLiveData<Boolean> = MutableLiveData(false)

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
                    addressData.value = data?.extras?.getString("address")
                    val splitArray = addressData.value?.split(",")
                    zoneCode.value = splitArray!![0]
                    address.value = splitArray!![1]
                }
            }
        }
    }


}