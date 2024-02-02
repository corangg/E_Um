package com.example.appointment.ui.fragment.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.ui.adapter.ChatListAdapter
import com.example.appointment.ui.activity.chat.ChatActivity
import com.example.appointment.databinding.FragmentChatBinding
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewmodel


class Chat_Fragment : BaseFragment<FragmentChatBinding>(), OnItemClickListener {
    private val mainViewmodel: MainViewmodel by activityViewModels()
    private lateinit var adapter: ChatListAdapter
    override fun layoutResId(): Int {
        return R.layout.fragment_chat_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }


    override fun onResume() {
        mainViewmodel.fnChatRoomList()
        super.onResume()
    }

    override fun onItemClick(position: Int) {
        mainViewmodel.chatRoomFriendNickName.value = mainViewmodel.chatRoomProfileArray.value!![position].nickname
        mainViewmodel.chatRoomProfileURL.value = mainViewmodel.chatProfileArray.value!![position]
        mainViewmodel.fnChatStart(mainViewmodel.chatRoomProfileArray.value!![position].email)
    }

    override fun setObserve(){
        mainViewmodel.chatRoomProfileArray.observe(viewLifecycleOwner, Observer {
            binding.recycleChat.layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(mainViewmodel.chatRoomProfileArray.value, mainViewmodel.chatProfileArray.value,this)
            binding.recycleChat.adapter=adapter
            adapter.setList(it!!)
            binding.recycleChat.addItemDecoration(
                DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            )
        })

        mainViewmodel.startChatActivity.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent = Intent(requireContext(), ChatActivity::class.java)
                intent.putExtra("friendnickname",mainViewmodel.chatRoomFriendNickName.value)
                intent.putExtra("chatRoomName",mainViewmodel.chatRoomName.value)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                intent.putExtra("friendProfileURL",mainViewmodel.chatRoomProfileURL.value)
                intent.putExtra("chatCount",mainViewmodel.chatCount.value)
                startActivity(intent)
            }
        }
    }

}