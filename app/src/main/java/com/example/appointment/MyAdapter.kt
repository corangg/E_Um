package com.example.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appointment.databinding.ItemAddressBinding
import com.example.appointment.databinding.ItemAlarmBinding
import com.example.appointment.databinding.ItemChatListBinding
import com.example.appointment.databinding.ItemFriendListBinding
import com.example.appointment.databinding.ItemMapAddressBinding
import com.example.appointment.databinding.ItemMessageGetBinding
import com.example.appointment.databinding.ItemMessageGetProfileBinding
import com.example.appointment.databinding.ItemMessageSendBinding
import com.example.appointment.databinding.ItemScheduleAlarmBinding
import com.example.appointment.databinding.ItemScheduleBinding
import com.example.appointment.main.MainViewmodel
import com.example.appointment.main.fragment.friend.FriendProfile_Fragment
import com.example.appointment.model.ChatDataModel
import com.example.appointment.model.ChatRoomData
import com.example.appointment.model.FriendRequestAlarmData

import com.example.appointment.model.PlaceItem
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.ScheduleSet
import org.checkerframework.checker.units.qual.mm

class AddressViewHolder(val binding: ItemAddressBinding): RecyclerView.ViewHolder(binding.root)
class AlarmViewHolder(val binding: ItemAlarmBinding): RecyclerView.ViewHolder(binding.root)
class FriendListViewHolder(val binding: ItemFriendListBinding): RecyclerView.ViewHolder(binding.root)
class ChatSendViewHolder(val binding: ItemMessageSendBinding): RecyclerView.ViewHolder(binding.root)
class ChatGetFirstViewHolder(val binding: ItemMessageGetProfileBinding): RecyclerView.ViewHolder(binding.root)
class ChatGetViewHolder(val binding: ItemMessageGetBinding): RecyclerView.ViewHolder(binding.root)
class ChatListViewHolder(val binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root)
class MapSearchViewHolder(val binding: ItemMapAddressBinding): RecyclerView.ViewHolder(binding.root)
class ScheduleListViewHolder(val binding: ItemScheduleBinding): RecyclerView.ViewHolder(binding.root)
class ScheduleAlarmListViewHolder(val binding: ItemScheduleAlarmBinding): RecyclerView.ViewHolder(binding.root)


class AddressAdapter(val addressTitledatas: MutableList<String>?,val addressdatas: MutableList<String>?,val mainaddress: MutableList<Boolean>?,val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var editBoolean = false

    interface OnItemClickListener {
        fun onItemClickDelete(position: Int)
        fun onItemClickTitle(address : String)
    }

    override fun getItemCount(): Int {
        return addressTitledatas?.size ?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = AddressViewHolder(ItemAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as AddressViewHolder).binding

        binding.addressTitle.text=addressTitledatas!![position]
        binding.address.text=addressdatas!![position]
        mainaddress!![position]
        if(mainaddress!![position]){
            holder.binding.btnTitle.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
        }else{
            holder.binding.btnTitle.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round)
            holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round)
        }

        binding.btnDelAddress.setOnClickListener {
            if(!mainaddress!![position]){
                addressTitledatas.removeAt(position)
                addressdatas.removeAt(position)
                notifyItemRemoved(position)
                onItemClickListener.onItemClickDelete(position)
            }
        }

        binding.btnTitleAddress.setOnClickListener {

            if(!mainaddress!![position]){
                holder.binding.btnTitle.visibility = View.VISIBLE
                mainaddress[position] = true
                for (i in 0..mainaddress.size-1)
                {
                    if(i != position){
                        mainaddress[i] = false
                    }
                }

                holder.binding.btnTitleAddress.setBackgroundResource(R.drawable.btn_round_gray)
                holder.binding.btnDelAddress.setBackgroundResource(R.drawable.btn_round_gray)
            }
            onItemClickListener.onItemClickTitle(addressdatas[position])
        }

        if (editBoolean) {
            holder.binding.btnDelAddress.visibility = View.VISIBLE
            holder.binding.btnTitleAddress.visibility = View.VISIBLE
        } else {
            holder.binding.btnDelAddress.visibility = View.INVISIBLE
            holder.binding.btnTitleAddress.visibility = View.INVISIBLE
        }
    }

    fun setItemEditable(editable: Boolean) {
        editBoolean = editable
    }
}

class AlarmAdapter(val alarmList: MutableList<FriendRequestAlarmData>, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClickAccept(email: String,nickName:String)
        fun onItemClickRefuse(email: String,nickName:String)
    }

    override fun getItemCount(): Int {
        return alarmList?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = AlarmViewHolder(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as AlarmViewHolder).binding

        binding.textRequest.text = alarmList!![position].nickname + "님에게 친구 요청이 들어왔습니다."

        binding.btnAccept.setOnClickListener {
            onItemClickListener.onItemClickAccept(alarmList[position].email, alarmList[position].nickname)
        }
        binding.btnRefuse.setOnClickListener {
            onItemClickListener.onItemClickRefuse(alarmList[position].email, alarmList[position].nickname)
        }
    }
}

class FriendAdapter(val friendProfile:MutableList<ProfileDataModel>?, val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    override fun getItemCount(): Int  = friendProfile?.size?:0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    = FriendListViewHolder(ItemFriendListBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FriendListViewHolder).binding

        binding.textNickname.text = friendProfile!![position].nickname
        binding.textStatusmessage.text = friendProfile!![position].statusmessage

        if(friendProfile!![position].imgURL != ""){
            Glide.with(holder.itemView).load(friendProfile!![position].imgURL).into(binding.imgProfile)
        }

        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}


class ChatAdapter(val chatMessage:MutableList<ChatDataModel>?,val email:String?,val profileURL:String?,val friendNickname:String?):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TYPE_SEND = 1
    private val TYPE_GETFIRST = 2
    private val TYPE_GET = 3

    override fun getItemViewType(position: Int): Int {
        return if(chatMessage!![position].email==email){
            TYPE_SEND
        }else if(position == 0||chatMessage!![position].email!=chatMessage!![position-1].email){//position-1이 없음
            TYPE_GETFIRST
        }else{
            TYPE_GET
        }
    }

    override fun getItemCount(): Int {
        return chatMessage?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when (viewType){
           TYPE_SEND->{
               ChatSendViewHolder(ItemMessageSendBinding.inflate(LayoutInflater.from(parent.context),parent,false))
           }
           TYPE_GETFIRST->{
               ChatGetFirstViewHolder(ItemMessageGetProfileBinding.inflate(LayoutInflater.from(parent.context),parent,false))
           }
           TYPE_GET->{
               ChatGetViewHolder(ItemMessageGetBinding.inflate(LayoutInflater.from(parent.context),parent,false))
           }
           else->throw IllegalArgumentException("Invalid view type")
       }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType){
            TYPE_SEND->{
                val binding:ItemMessageSendBinding=(holder as ChatSendViewHolder).binding
                binding.textSendMessage.text = chatMessage!![position].message
            }
            TYPE_GETFIRST->{
                val binding:ItemMessageGetProfileBinding=(holder as ChatGetFirstViewHolder).binding
                binding.textMessage.text = chatMessage!![position].message
                binding.textNickname.text = friendNickname
                if(profileURL != ""){
                    Glide.with(holder.itemView).load(profileURL).into(binding.imgProfile)
                }
            }
            TYPE_GET->{
                val binding:ItemMessageGetBinding=(holder as ChatGetViewHolder).binding
                binding.textSendMessage.text = chatMessage!![position].message
            }
        }
    }
}


class ChatListAdapter(val chatRoomData:MutableList<ChatRoomData>?, val chatRoomProfile:MutableList<String?>?,val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return chatRoomData?.size?:0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatListViewHolder(ItemChatListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding:ItemChatListBinding=(holder as ChatListViewHolder).binding
        binding.textNickname.text = chatRoomData!![position].nickname
        binding.textLastMessage.text = chatRoomData!![position].lastMessage
        binding.textNotCheckMessageCount.text = chatRoomData!![position].notCheckChat.toString()

        if(chatRoomData!![position].notCheckChat == 0){
            binding.notCheckMessageCount.visibility = View.GONE
        }

        if(chatRoomProfile!![position] != ""){
            Glide.with(holder.itemView).load(chatRoomProfile!![position]).into(binding.imgProfile)
        }

        binding.profile.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}


class MapSearchAdapter(val mapinfoList:MutableList<PlaceItem>,val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return mapinfoList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MapSearchViewHolder(ItemMapAddressBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding:ItemMapAddressBinding=(holder as MapSearchViewHolder).binding

        val searchName : String = mapinfoList[position].title.replace("<b>","").replace("</b>","")
        binding.name.text = searchName
        binding.roadAddress.text = mapinfoList[position].roadAddress

        binding.itemSearch.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}


class ScheduleListAdapter(val scheduleDataList : MutableList<ScheduleSet>,val onItemClickListener: OnItemClickListener, val onItemLongClickListener: OnItemLongClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int,status: String)
    }
    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    override fun getItemCount(): Int {
        return scheduleDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleListViewHolder(ItemScheduleBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding:ItemScheduleBinding = (holder as ScheduleListViewHolder).binding

        val YYYY = scheduleDataList[position].time.substring(0,4)
        val MM = scheduleDataList[position].time.substring(4,6)
        val DD = scheduleDataList[position].time.substring(6,8)
        val hh = scheduleDataList[position].time.substring(8,10)
        val mm = scheduleDataList[position].time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textNickname.text = scheduleDataList[position].nickname
        binding.textAddress.text = scheduleDataList[position].meetingPlaceKeyword + ", " + scheduleDataList[position].meetingPlaceAddress
        binding.textMeetingtime.text = date
        if(scheduleDataList[position].status == "wait"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "대기중"
        }else if(scheduleDataList[position].status == "refuse"){
            binding.btnScheduleStatus.visibility = View.VISIBLE
            binding.btnScheduleStatus.text = "거절됨"
        }

        if(scheduleDataList[position].profileImgURL != ""){
            Glide.with(holder.itemView).load(scheduleDataList[position].profileImgURL).into(binding.imgProfile)
        }
        binding.itemSchedule.setOnClickListener {
            onItemClickListener.onItemClick(position,scheduleDataList[position].status)
        }

        binding.itemSchedule.setOnLongClickListener {
            onItemLongClickListener.onItemLongClick(position)
            true
        }
    }
}


class ScheduleAlarmListAdapter(val scheduleDataList : MutableList<ScheduleSet>,val onItemClickListener: OnItemClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun getItemCount(): Int {
        return scheduleDataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ScheduleAlarmListViewHolder(ItemScheduleAlarmBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding:ItemScheduleAlarmBinding = (holder as ScheduleAlarmListViewHolder).binding

        val YYYY = scheduleDataList[position].time.substring(0,4)
        val MM = scheduleDataList[position].time.substring(4,6)
        val DD = scheduleDataList[position].time.substring(6,8)
        val hh = scheduleDataList[position].time.substring(8,10)
        val mm = scheduleDataList[position].time.substring(10,12)
        val date = YYYY+"년"+MM+"월"+DD+"일"+hh+"시"+ mm +"분"

        binding.textAlarmText.text = scheduleDataList[position].nickname + "님이 약속을 잡고 싶어합니다."
        binding.textScheduleTime.text = date

        binding.itemScheduleAlarm.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }
}



