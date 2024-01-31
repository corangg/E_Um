package com.example.appointment.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.appointment.R
import com.example.appointment.databinding.ActivityLoginBinding
import com.example.appointment.main.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    val loginViewModel:Login_Viewmodel by viewModels()

    var googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result->
        val data = result.data
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        account.idToken
        loginViewModel.firebaseAuthWithGoogle(account.idToken)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        binding.viewmodel =loginViewModel
        binding.lifecycleOwner =this

        loginViewModel.checkAuth()





        setObserve()
    }

    fun setObserve(){
        loginViewModel.showSignup_Fragment.observe(this){
            if(it){
                startActivity(Intent(this,SignupActivity::class.java))
            }
        }
        loginViewModel.showMainActivity.observe(this){
            if(it){
                finish()
                val intent: Intent = Intent(this,MainActivity::class.java)
                intent.putExtra("email",loginViewModel.email)
                startActivity(intent)
            }
        }
        loginViewModel.checkLogin.observe(this){
            if(it){
                finish()
                val intent: Intent = Intent(this,MainActivity::class.java)
                intent.putExtra("email",loginViewModel.email)
                startActivity(intent)
            }
        }

        loginViewModel.loginfail.observe(this){
            when(it){
                0-> Toast.makeText(this,"아이디 와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show()
                1-> Toast.makeText(this,"계정이 인증되지 않았습니다. 등록하신 이메일에서 계정을 인증해 주세요.", Toast.LENGTH_LONG).show()
                2-> Toast.makeText(this,"계정이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                3-> Toast.makeText(this,"아이디나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }
}