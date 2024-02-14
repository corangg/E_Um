package com.example.appointment.ui.fragment.friend

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.appointment.ui.adapter.FriendListAdapter
import com.example.appointment.R
import com.example.appointment.data.RequestCode
import com.example.appointment.ui.activity.friend.FriendAddActivity
import com.example.appointment.ui.activity.friend.FriendAlarmActivity
import com.example.appointment.databinding.FragmentFriendBinding
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.ui.fragment.AdapterFragment
import com.example.appointment.viewmodel.MainViewModel

class Friend_Fragment : AdapterFragment<FragmentFriendBinding>(), OnItemClickListener{
    private val mainViewmodel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_friend_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        if(mainViewmodel.profileImgURL.value != ""){
            Glide.with(this).load(mainViewmodel.profileImgURL.value).into(binding.imgProfile)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mainViewmodel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_friend, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_friend_add->{
            mainViewmodel.selectFriendAddItem()
            true
        }
        R.id.menu_friend_alarm->{
            mainViewmodel.selectFriendAlarmItem()
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        mainViewmodel.selectFriend(position)
    }

    override fun setObserve(){
        mainViewmodel.friendRequestAlarmStatus.observe(viewLifecycleOwner){
            if(it){
                binding.friendAlarmIcOn.visibility = View.VISIBLE

            }else{
                binding.friendAlarmIcOn.visibility = View.GONE
            }
        }

        mainViewmodel.friendsProfileList.observe(viewLifecycleOwner){
            setAdapter(binding.recycleFriends,FriendListAdapter(this),it,true)

        }

        mainViewmodel.startFriendAddActivity.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent=Intent(requireActivity(), FriendAddActivity::class.java)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                startActivity(intent)
            }
        }

        mainViewmodel.startFriendAlarmActivity.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent=Intent(requireActivity(), FriendAlarmActivity::class.java)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value.toString())
                startActivityForResult(intent, RequestCode.ACCEPT_FRIEND_REQUEST_CODE)
            }
        }

        mainViewmodel.startFriendProfileFragment.observe(viewLifecycleOwner){
            if(it){
                getTransaction().replace(R.id.fragment_friend_profile, FriendProfile_Fragment()).addToBackStack(null).commit()
            }
        }
    }
}