package com.example.appointment.ui.fragment.friend

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.appointment.R
import com.example.appointment.ui.activity.chat.ChatActivity
import com.example.appointment.databinding.FragmentFriendProfileBinding
import com.example.appointment.viewmodel.MainViewmodel
import com.example.appointment.ui.fragment.schedule.ScheduleCalendar_Fragment

class FriendProfile_Fragment : Fragment() {
    private val mainViewmodel: MainViewmodel by activityViewModels()
    private lateinit var binding: FragmentFriendProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFriendProfileBinding.inflate(inflater, container, false)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this

        mainViewmodel.fnFriendProfileSet()

        setObserve()
        return binding.root
    }

    private fun setObserve(){
        mainViewmodel.friendImgURL.observe(viewLifecycleOwner, Observer {
            it?.let{
                if(mainViewmodel.friendImgURL.value != ""){
                    Glide.with(this).load(mainViewmodel.friendImgURL.value).into(binding.imgProfile)
                }
            }
        })

        mainViewmodel.startChatActivity.observe(viewLifecycleOwner){
            if(it){
                val intent:Intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra("friendnickname",mainViewmodel.selectFriendProfile.value!!.nickname)//friendNicknameMutableList.value!![mainViewmodel.selectFriendPosition])
                intent.putExtra("chatRoomName",mainViewmodel.chatRoomName.value)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                intent.putExtra("friendProfileURL",mainViewmodel.friendImgURL.value)
                intent.putExtra("chatCount",mainViewmodel.chatCount.value)
                startActivity(intent)
            }
        }

        mainViewmodel.friendDeleteSuccess.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }

        mainViewmodel.scheduleCalendarFragmentStart.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                transaction.replace(R.id.fragment_friend_profile, ScheduleCalendar_Fragment()).addToBackStack(null).commit()
            }
        }


    }
}