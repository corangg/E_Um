package com.example.appointment.ui.fragment.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.R
import com.example.appointment.data.RequestCode
import com.example.appointment.ui.adapter.ChatListAdapter
import com.example.appointment.ui.activity.chat.ChatActivity
import com.example.appointment.databinding.FragmentChatBinding
import com.example.appointment.ui.adapter.ChatAdapter
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.ui.fragment.AdapterFragment
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.MainViewModel


class Chat_Fragment : AdapterFragment<FragmentChatBinding>(), OnItemClickListener {
    private val mainViewmodel: MainViewModel by activityViewModels()

    override fun layoutResId(): Int {
        return R.layout.fragment_chat_
    }

    override fun initializeUI() {
        binding.viewmodel = mainViewmodel
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    }

    override fun onItemClick(position: Int) {
        mainViewmodel.fnSelectChat(position)
    }

    override fun onResume() {
        mainViewmodel.fnChatRoomList()
        super.onResume()
    }

    override fun setObserve(){
        mainViewmodel.chatRoomProfileList.observe(viewLifecycleOwner){
            setAdapter(binding.recycleChat,ChatListAdapter(this),it,true)
        }

        mainViewmodel.startChatActivity.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent = Intent(requireContext(), ChatActivity::class.java)

                intent.putExtra("friendnickname",mainViewmodel.chatRoomFriendNickName.value)
                intent.putExtra("chatRoomName",mainViewmodel.chatRoomName.value)
                intent.putExtra("nickname",mainViewmodel.profileNickname.value)
                intent.putExtra("friendProfileURL",mainViewmodel.chatFriendImg.value)
                intent.putExtra("chatCount",mainViewmodel.chatCount.value)

                startActivityForResult(intent, RequestCode.CHAT_REQUEST_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mainViewmodel.fnHandleActivityResult(requestCode, resultCode, data)
    }

}