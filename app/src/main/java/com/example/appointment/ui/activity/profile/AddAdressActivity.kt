package com.example.appointment.ui.activity.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.example.appointment.DB.AddressDBHelper
import com.example.appointment.R
import com.example.appointment.data.RequestCode
import com.example.appointment.databinding.ActivityAddAdressBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.profile.AddAddressViewModel

class AddAdressActivity : BaseActivity<ActivityAddAdressBinding>() {
    val viewModel: AddAddressViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_add_adress
    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.resultAddress(requestCode,resultCode,data)
    }

    override fun setObserve(){
        viewModel.searchAddress.observe(this){
            if(it){
                Intent(this, AdressSearchActivity::class.java).apply {
                    startActivityForResult(this, RequestCode.ADDRESS_REQUEST_CODE)
                }
            }
        }

        viewModel.editAddress.observe(this){
            if(it){
                val fullAddress = viewModel.addressData + viewModel.detailAddress.value
                val email = intent.getStringExtra("email")

                val db = AddressDBHelper(this,email).writableDatabase
                db.execSQL("insert into ADDRESS_TB(name,address) values(?,?)", arrayOf(viewModel.addressName.value,fullAddress))
                db.close()

                intent.putExtra("addressName",viewModel.addressName.value)
                intent.putExtra("address",fullAddress)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }
}