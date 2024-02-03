package com.example.appointment.ui.activity

import android.app.AlarmManager
import android.app.PendingIntent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.appointment.R
import com.example.appointment.ui.fragment.schedule.Schedule_Fragment
import com.example.appointment.databinding.ActivityMainBinding
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.ui.fragment.chat.Chat_Fragment
import com.example.appointment.ui.fragment.friend.Friend_Fragment
import com.example.appointment.ui.fragment.profile.Profile_Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//@AndroidEntryPoint
//@Inject
//lateinit var profileFragment: Profile_Fragment
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val mainViewmodel : MainViewmodel by viewModels()


    @Inject lateinit var profileFragment: Profile_Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner=this

        mainViewmodel.fnFriendList()//바로쳇프래그맨트가면 친구리스트 안만들어져 있어서 메인에서 한번 초기화

        if(intent.getStringExtra("path")=="openNotification"){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_main, Schedule_Fragment()).commit()
        }

        setObserve()
    }

    fun setObserve(){
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

