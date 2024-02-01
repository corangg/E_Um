package com.example.appointment.UI.Activity.LogIn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.appointment.UI.Activity.Profile.AdressSearchActivity
import com.example.appointment.R
import com.example.appointment.databinding.ActivitySignupBinding
import com.example.appointment.UI.Activity.Profile.AdressSearchActivity.Companion.ADDRESS_REQUEST_CODE
import com.example.appointment.ViewModel.SignUp.Signup_Viewmodel

class SignupActivity : AppCompatActivity() {
    lateinit var binding : ActivitySignupBinding

    val signupViewModel : Signup_Viewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup)
        binding.viewmodel = signupViewModel
        binding.lifecycleOwner =this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setObserve()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ADDRESS_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    signupViewModel.addressData.value = data?.extras?.getString("address")
                    signupViewModel.splitAddress()
                }

            }
        }
    }

    fun setObserve(){
        signupViewModel.passwordCheck.observe(this){
            if(it){
                Toast.makeText(this,"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        signupViewModel.signupSuccess.observe(this){
            if(it){
                finish()
                startActivity(Intent(this, LoginActivity::class.java))
                Toast.makeText(this,"회원가입 성공",Toast.LENGTH_SHORT).show()
            }
        }
        signupViewModel.signupIdCheck.observe(this){
            if(!it){
                Toast.makeText(this,"동일한 계정이 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        signupViewModel.searchAddress.observe(this){
            if(it){
                Intent(this, AdressSearchActivity::class.java).apply {
                    startActivityForResult(this,ADDRESS_REQUEST_CODE)
                }
            }
        }
    }
}