package com.example.appointment.ui.activity.profile

import android.app.ProgressDialog.show
import android.widget.Toast
import androidx.activity.viewModels
import com.example.appointment.R
import com.example.appointment.databinding.ActivityPasswordEditBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.profile.PasswordEditViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordEditActivity : BaseActivity<ActivityPasswordEditBinding>() {

    val passwordEditViewModel : PasswordEditViewModel by viewModels()

    override fun layoutResId(): Int {
        return R.layout.activity_password_edit
    }

    override fun initializeUI() {
        binding.viewmodel = passwordEditViewModel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun setObserve(){
        passwordEditViewModel.passwordUpdate.observe(this){
            when(it){
                0-> toast("비밀번호 확인 완료")
                1-> toast("비밀번호가 틀립니다.")
                2-> toast("기존 비밀번호 확인이 필요합니다.")
                3-> toast( "새 비밀번호가 일치하지 않습니다.")
                4->{
                    toast("비밀번호 변경 완료")
                    finish()
                }
                5-> toast("비밀번호 변경에 실패했습니다.")
            }
        }
    }
}