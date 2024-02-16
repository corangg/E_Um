package com.example.appointment.ui.activity.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.appointment.AddressDBHelper
import com.example.appointment.ui.activity.profile.AdressSearchActivity
import com.example.appointment.R
import com.example.appointment.data.RequestCode.Companion.ADDRESS_REQUEST_CODE
import com.example.appointment.data.ToastMessage
import com.example.appointment.databinding.ActivitySignupBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.ui.activity.login.LoginActivity
import com.example.appointment.viewmodel.signup.Signup_Viewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>() {
    val signupViewModel : Signup_Viewmodel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_signup
    }

    override fun initializeUI() {
        binding.viewmodel = signupViewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        signupViewModel.fnHandleActivityResult(requestCode, resultCode, data)
    }

    override fun setObserve(){
        signupViewModel.passwordCheck.observe(this){
            ToastMessage.mismatchPassword(this)
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
        /*signupViewModel.titleAddress.observe(this){
            val db = AddressDBHelper(this,signupViewModel.signUpEmail.value).writableDatabase//signupViewModel.signUpEmail.value가 비어서 그럼
            db.execSQL("insert into ADDRESS_TB(name,address) values(?,?)", arrayOf("집",signupViewModel.titleAddress.value))
            db.close()
        }*/
    }
}
