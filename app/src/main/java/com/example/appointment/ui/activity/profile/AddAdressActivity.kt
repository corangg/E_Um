package com.example.appointment.ui.activity.profile

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.appointment.AddressDBHelper
import com.example.appointment.R
import com.example.appointment.databinding.ActivityAddAdressBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.logIn.Login_Viewmodel
import com.example.appointment.viewmodel.signup.Signup_Viewmodel

class AddAdressActivity : BaseActivity<ActivityAddAdressBinding>() {

    val viewModel: Signup_Viewmodel by viewModels()

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

        when (requestCode) {
            AdressSearchActivity.ADDRESS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    viewModel.addressData.value = data?.extras?.getString("address")
                    viewModel.splitAddress()
                }

            }
        }
    }

    override fun setObserve(){
        viewModel.searchAddress.observe(this){
            if(it){
                Intent(this, AdressSearchActivity::class.java).apply {
                    startActivityForResult(this, AdressSearchActivity.ADDRESS_REQUEST_CODE)
                }
            }
        }
        viewModel.addressAdd.observe(this){
            if(it){
                val email = intent.getStringExtra("email")
                val db = AddressDBHelper(this,email).writableDatabase
                db.execSQL("insert into ADDRESS_TB(name,address) values(?,?)", arrayOf(viewModel.addressName.value,viewModel.address))
                db.close()

                intent.putExtra("addressName",viewModel.addressName.value)
                intent.putExtra("address",viewModel.address)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }
        }
    }
}