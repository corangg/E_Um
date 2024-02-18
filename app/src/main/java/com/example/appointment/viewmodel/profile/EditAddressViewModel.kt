package com.example.appointment.viewmodel.profile

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.DB.AddressDBHelper
import com.example.appointment.data.StartEvent
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils
import com.example.appointment.data.AddressData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditAddressViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    val titleAddress : MutableLiveData<String> = MutableLiveData("")

    val startAddAddress : MutableLiveData<Boolean> = MutableLiveData(false)
    val addressEdit : MutableLiveData<Boolean> = MutableLiveData(false)
    val addAddressData : MutableLiveData<AddressData> = MutableLiveData()
    val clickItem : MutableLiveData<Boolean> = MutableLiveData(false)
    val addressDataList : MutableLiveData<MutableList<AddressData>> = MutableLiveData()
    val addressList : MutableList<AddressData> = mutableListOf()

    fun selectMenuAdd(){
        StartEvent(startAddAddress)
    }

    fun seledtMenuEdit(){
        addressEdit.value = addressEdit.value?.not() ?: true
    }

    fun clickDeleteItem(position:Int,dbHelper: AddressDBHelper){
        dbHelper.deleteRow(position.toLong())
        StartEvent(clickItem)
    }

    fun clickTitleItem(address:String){
        titleAddress.value = address
        StartEvent(clickItem)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when (requestCode) {
            RequestCode.ADD_ADDRESS_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        val addressData = AddressData(
                            it.getStringExtra("addressName")!!,
                            it.getStringExtra("address")!!,
                            false
                        )
                        addressList.add(addressData)
                        addressDataList.value = addressList

                        addAddressData.value = addressData
                    }
                }
            }

        }
    }

    fun readDB(intent: Intent, context: Context){
        titleAddress.value = intent.getStringExtra("mainaddress")

        val db = AddressDBHelper(context, Utils.auth.currentUser?.email).readableDatabase
        val cursor = db.rawQuery("select * from ADDRESS_TB",null)
        cursor.run{
            while (moveToNext()){
                if(titleAddress.value == cursor.getString(2)){
                    val addressData = AddressData(
                        cursor.getString(1),
                        cursor.getString(2),
                        true
                    )
                    addressList.add(addressData)
                }else{
                    val addressData = AddressData(
                        cursor.getString(1),
                        cursor.getString(2),
                        false
                    )
                    addressList.add(addressData)
                }
            }
        }
        addressDataList.value = addressList
        db.close()
    }
}