package com.example.appointment.ui.activity

import androidx.activity.viewModels
import com.example.appointment.R
import com.example.appointment.ui.fragment.schedule.Schedule_Fragment
import com.example.appointment.databinding.ActivityMainBinding
import com.example.appointment.ui.fragment.chat.Chat_Fragment
import com.example.appointment.ui.fragment.friend.Friend_Fragment
import com.example.appointment.ui.fragment.profile.Profile_Fragment
import com.example.appointment.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    val mainViewmodel : MainViewModel by viewModels()

    @Inject lateinit var profileFragment: Profile_Fragment

    override fun layoutResId(): Int {
        return R.layout.activity_main
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel

        if(intent.getStringExtra("path")=="openNotification"){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Schedule_Fragment()).commit()
        }
    }

    override fun setObserve(){
        mainViewmodel.selectFragment.observe(this){
            when(it.toString()){
                "profile"->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_main, profileFragment/*Profile_Fragment()*/).commit()
                }
                "friends"->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Friend_Fragment()).commit()
                }
                "chat"->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Chat_Fragment()).commit()
                }
                "schedule"->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Schedule_Fragment()).commit()
                }
                else->{
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Profile_Fragment()).commit()
                }
            }
        }
    }
}

