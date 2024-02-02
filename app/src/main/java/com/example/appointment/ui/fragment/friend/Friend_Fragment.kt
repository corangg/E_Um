package com.example.appointment.ui.fragment.friend

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.appointment.ui.adapter.FriendListAdapter
import com.example.appointment.R
import com.example.appointment.ui.activity.friend.FriendAddActivity
import com.example.appointment.ui.activity.friend.FriendAlarmActivity
import com.example.appointment.databinding.FragmentFriendBinding
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewmodel


class Friend_Fragment : BaseFragment<FragmentFriendBinding>(), OnItemClickListener{
    private val mainViewmodel: MainViewmodel by activityViewModels()
    private lateinit var adapter: FriendListAdapter

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

        mainViewmodel.fnFriendAlarmReceive()
    }

    override fun onResume() {
        mainViewmodel.fnFriendList()
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_friend, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_friend_add->{
            val intent: Intent=Intent(requireActivity(), FriendAddActivity::class.java)
            intent.putExtra("nickname",mainViewmodel.profileNickname.value.toString())
            startActivity(intent)
            true
        }
        R.id.menu_friend_alarm->{
            val intent: Intent=Intent(requireActivity(), FriendAlarmActivity::class.java)
            intent.putExtra("nickname",mainViewmodel.profileNickname.value.toString())
            startActivity(intent)
            true
        }
        else-> super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        mainViewmodel.selectFriendProfile.value = mainViewmodel.friendsProfileList.value!![position]

        val fragmentManager = (context as AppCompatActivity).supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_friend_profile, FriendProfile_Fragment()).addToBackStack(null).commit()
    }

    override fun setObserve(){
        mainViewmodel.friendAlarmReceiveStatus.observe(viewLifecycleOwner){
            if(it){
                binding.friendAlarmIcOn.visibility = View.VISIBLE

            }else{
                binding.friendAlarmIcOn.visibility = View.GONE
            }
        }

        mainViewmodel.friendsProfileList.observe(viewLifecycleOwner){
            binding.recycleFriends.layoutManager = LinearLayoutManager(context)
            adapter = FriendListAdapter(it,this)
            binding.recycleFriends.adapter=adapter

            binding.recycleFriends.addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
            adapter.setList(it!!)
        }

    }




}