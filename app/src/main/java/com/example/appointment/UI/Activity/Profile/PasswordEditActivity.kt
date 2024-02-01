package com.example.appointment.UI.Activity.Profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.appointment.R
import com.example.appointment.databinding.ActivityPasswordEditBinding
import com.example.appointment.ViewModel.MainViewmodel

class PasswordEditActivity : AppCompatActivity() {
    lateinit var binding : ActivityPasswordEditBinding

    val mainViewmodel : MainViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_password_edit)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setObserve()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun setObserve(){
        mainViewmodel.passwordUpdate.observe(this){
            when(it){
                0-> Toast.makeText(this,"비밀번호 확인 완료",Toast.LENGTH_SHORT).show()
                1-> Toast.makeText(this,"비밀번호가 틀립니다.",Toast.LENGTH_SHORT).show()
                2-> Toast.makeText(this,"기존 비밀번호 확인이 필요합니다.",Toast.LENGTH_SHORT).show()
                3-> Toast.makeText(this, "새 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                4->{
                    Toast.makeText(this,"비밀번호 변경 완료",Toast.LENGTH_SHORT).show()
                    finish()
                }
                5-> Toast.makeText(this,"비밀번호 변경에 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}