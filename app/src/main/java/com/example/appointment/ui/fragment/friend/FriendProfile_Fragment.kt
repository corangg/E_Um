package com.example.appointment.ui.fragment.friend

import android.content.Intent
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.appointment.R
import com.example.appointment.ui.activity.chat.ChatActivity
import com.example.appointment.databinding.FragmentFriendProfileBinding
import com.example.appointment.model.ChatStartData
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.ui.fragment.schedule.ScheduleCalendar_Fragment
import com.example.appointment.viewmodel.profile.ProfileViewModel

class FriendProfile_Fragment : BaseFragment<FragmentFriendProfileBinding>() {
    private val mainViewmodel: ProfileViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_friend_profile_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel

        if(mainViewmodel.selectFriendProfile.value!!.imgURL != ""){
            Glide.with(this).load(mainViewmodel.selectFriendProfile.value!!.imgURL).into(binding.imgProfile)
        }
    }

    override fun setObserve(){

        mainViewmodel.startChatActivity.observe(viewLifecycleOwner){
            if(it){
                val intent:Intent = Intent(requireContext(), ChatActivity::class.java)

                intent.putExtra("friendnickname",mainViewmodel.selectFriendProfile.value!!.nickname)//friendNicknameMutableList.value!![mainViewmodel.selectFriendPosition])
                intent.putExtra("chatRoomName",mainViewmodel.chatRoomName.value)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                intent.putExtra("friendProfileURL",mainViewmodel.selectFriendProfile.value!!.imgURL)
                intent.putExtra("chatCount",mainViewmodel.chatCount.value)

                startActivity(intent)
            }
        }

        mainViewmodel.startScheduleCalendarFragment.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                val transaction = fragmentManager.beginTransaction()

                transaction.replace(R.id.fragment_friend_profile, ScheduleCalendar_Fragment()).addToBackStack(null).commit()
            }
        }

        mainViewmodel.friendDeleteSuccess.observe(viewLifecycleOwner){
            if(it){
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
        }
    }
}