package com.example.appointment.UI.Activity.Profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.appointment.AddressDBHelper
import com.example.appointment.R
import com.example.appointment.databinding.ActivityAddAdressBinding
import com.example.appointment.ViewModel.LogIn.Login_Viewmodel
import com.example.appointment.ViewModel.SignUp.Signup_Viewmodel

class AddAdressActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddAdressBinding

    val signupViewmodel : Signup_Viewmodel by viewModels()
    val loginViewmodel: Login_Viewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_adress)
        binding.singviewmodel = signupViewmodel
        binding.lifecycleOwner = this

        setObserve()

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
                    signupViewmodel.addressData.value = data?.extras?.getString("address")
                    signupViewmodel.splitAddress()
                }

            }
        }
    }

    fun setObserve(){
        signupViewmodel.searchAddress.observe(this){
            if(it){
                Intent(this, AdressSearchActivity::class.java).apply {
                    startActivityForResult(this, AdressSearchActivity.ADDRESS_REQUEST_CODE)
                }
            }
        }
        signupViewmodel.addressAdd.observe(this){
            if(it){
                val email = intent.getStringExtra("email")
                val db = AddressDBHelper(this,email).writableDatabase
                loginViewmodel.email
                db.execSQL("insert into ADDRESS_TB(name,address) values(?,?)", arrayOf(signupViewmodel.addressName.value,signupViewmodel.address))
                db.close()

                //val intent = intent


                intent.putExtra("addressName",signupViewmodel.addressName.value)
                intent.putExtra("address",signupViewmodel.address)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }
        }
    }
}