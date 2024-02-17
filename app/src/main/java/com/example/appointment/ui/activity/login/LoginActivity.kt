package com.example.appointment.ui.activity.login

import android.content.Intent
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.example.appointment.R
import com.example.appointment.databinding.ActivityLoginBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.ui.activity.MainActivity
import com.example.appointment.ui.activity.signup.SignupActivity
import com.example.appointment.viewmodel.logIn.Login_Viewmodel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    val loginViewModel: Login_Viewmodel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_login
    }

    override fun initializeUI() {
        binding.viewmodel =loginViewModel
    }

    override fun setObserve(){
        loginViewModel.showSignupActivity.observe(this){
            if(it){
                startActivity(Intent(this, SignupActivity::class.java))
            }
        }
        loginViewModel.showMainActivity.observe(this){
            if(it){
                finish()
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        loginViewModel.checkLogin.observe(this){
            if(it){
                finish()
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        loginViewModel.loginfail.observe(this){
            when(it){
                0-> toast("아이디 와 비밀번호를 입력하세요")
                1-> toast("계정이 인증되지 않았습니다. 등록하신 이메일에서 계정을 인증해 주세요.")
                2-> toast("계정이 존재하지 않습니다.")
                3-> toast("비밀번호가 일치하지 않습니다.")
            }
        }
    }
}