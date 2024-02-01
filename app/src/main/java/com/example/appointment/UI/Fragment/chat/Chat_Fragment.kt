package com.example.appointment.UI.Fragment.chat

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
import com.example.appointment.UI.Adapter.ChatListAdapter
import com.example.appointment.UI.Activity.Chat.ChatActivity
import com.example.appointment.databinding.FragmentChatBinding
import com.example.appointment.ViewModel.MainViewmodel


class Chat_Fragment : Fragment(), ChatListAdapter.OnItemClickListener {
    private lateinit var binding:FragmentChatBinding
    private val mainViewmodel: MainViewmodel by activityViewModels()
    private lateinit var adapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this



        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        setObserve()
        return binding.root
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

    private fun setObserve(){
        mainViewmodel.chatRoomProfileArray.observe(viewLifecycleOwner, Observer {
            binding.recycleChat.layoutManager = LinearLayoutManager(context)
            adapter = ChatListAdapter(mainViewmodel.chatRoomProfileArray.value, mainViewmodel.chatProfileArray.value,this)
            binding.recycleChat.adapter=adapter
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