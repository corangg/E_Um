package com.example.appointment.viewmodel.profile

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.model.AddressData
import com.example.appointment.viewmodel.BaseViewModel

class EditAddressViewModel(application: Application): AndroidViewModel(application) {

    val titleAddress : MutableLiveData<String> = MutableLiveData("")

    val startAddAddress : MutableLiveData<Boolean> = MutableLiveData(false)
    val addressEdit : MutableLiveData<Boolean> = MutableLiveData(false)
    val addAddressData : MutableLiveData<AddressData> = MutableLiveData()

    fun selectMenuAdd(){
        StartEvent(startAddAddress)
    }

    fun seledtMenuEdit(){
        addressEdit.value = addressEdit.value?.not() ?: true
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        var addressData : AddressData

        when (requestCode) {
            RequestCode.ADD_ADDRESS_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        addressData = AddressData(
                            it.getStringExtra("addressName")!!,
                            it.getStringExtra("address")!!,
                            false
                        )
                        addAddressData.value = addressData
                    }
                }
            }

        }
    }
}