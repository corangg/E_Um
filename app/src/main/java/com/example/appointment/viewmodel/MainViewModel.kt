package com.example.appointment.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.appointment.R

import com.example.appointment.StartEvent
import com.example.appointment.apiservice.APIData
import com.example.appointment.apiservice.NaverGeocode
import com.example.appointment.apiservice.TMapCarRoute
import com.example.appointment.apiservice.TMapPublicTransportRoute
import com.example.appointment.apiservice.TMapWalkRoute
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.data.Utils.Companion.database
import com.example.appointment.data.Utils.Companion.firestore
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.CarRouteRequest
import com.example.appointment.model.ChatRoomData
import com.example.appointment.model.EmailNicknameData
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.ScheduleSet
import com.example.appointment.model.ScheduleTime
import com.example.appointment.model.TransitRouteRequest
import com.example.appointment.model.WalkRouteRequest
import com.example.appointment.repository.ChatFragmentRepository
import com.example.appointment.repository.FriendFragmnetRepository
import com.example.appointment.repository.FriendProfileFagmentRepository
import com.example.appointment.repository.ProfileRepository
import com.example.appointment.repository.ScheduleSetFragmentRepository
import com.google.android.play.integrity.internal.al
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val profileRepository: ProfileRepository,
    private val friendFragmnetRepository: FriendFragmnetRepository,
    private val friendProfileFagmentRepository: FriendProfileFagmentRepository,
    private val chatFragmentRepository: ChatFragmentRepository,
    private val scheduleSetFragmentRepository: ScheduleSetFragmentRepository,
) : BaseViewModel(application) {

    val selectFragment : MutableLiveData<String> = MutableLiveData("")
    val profileNickname: MutableLiveData<String> = MutableLiveData("")
    val profileStatusMessage: MutableLiveData<String> = MutableLiveData("")
    val profileName: MutableLiveData<String> = MutableLiveData("")
    val profilePhone: MutableLiveData<String> = MutableLiveData("")
    val profileEmail: MutableLiveData<String> = MutableLiveData("")
    val profilePassword: MutableLiveData<String> = MutableLiveData("")
    val profileAddress: MutableLiveData<String> = MutableLiveData("")
    val scheduleHH : MutableLiveData<String> = MutableLiveData("")
    val scheduleMM : MutableLiveData<String> = MutableLiveData("")
    val meetingPlaceAddress :MutableLiveData<String> = MutableLiveData("")
    val publicTransportTime :MutableLiveData<String> = MutableLiveData("")
    val carTime :MutableLiveData<String> = MutableLiveData("")
    val walkTime : MutableLiveData<String> = MutableLiveData("")
    val scheduleAlarmHH : MutableLiveData<String> = MutableLiveData("")
    val scheduleAlarmMM : MutableLiveData<String> = MutableLiveData("")
    val scheduleDate : MutableLiveData<String> = scheduleSetFragmentRepository.scheduleDate

    val editProfileData : MutableLiveData<Boolean> = MutableLiveData(false)
    val openGallery : MutableLiveData<Boolean> = MutableLiveData(false)
    val imageUpload : MutableLiveData<Boolean> = MutableLiveData(false)
    val nickNameEditActivityStart : MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordEdit : MutableLiveData<Boolean> = MutableLiveData(false)
    val addressEditActivityStart : MutableLiveData<Boolean> = MutableLiveData(false)
    val logOutSuccess : MutableLiveData<Boolean> = MutableLiveData(false)
    val friendRequestAlarmStatus : MutableLiveData<Boolean> = MutableLiveData()
    val startFriendAddActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendAlarmActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendProfileFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    val startChatActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startScheduleCalendarFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    val friendDeleteSuccess : MutableLiveData<Boolean> = MutableLiveData(false)
    val startScheduleSetFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    val alarmSet : MutableLiveData<Boolean> = MutableLiveData(false)
    val scheduleAmPmSet : MutableLiveData<Boolean> = MutableLiveData(true)
    val startScheduleMapActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val meetingTimeOver : MutableLiveData<Boolean> = MutableLiveData()
    val appointmentRequestSuccess : MutableLiveData<Boolean> = MutableLiveData()

    val selectTransport : MutableLiveData<Int> = MutableLiveData(0)
    val transportCheck : MutableLiveData<Int> = MutableLiveData(-1)

    val friendsProfileList: MutableLiveData<MutableList<ProfileDataModel>> = MutableLiveData()
    val chatRoomProfileList: MutableLiveData<MutableList<ChatRoomData>> = chatFragmentRepository.chatRoomProfileList

    var profileImgURL : String = ""
    var chatRoomFriendNickName : String = ""
    var chatRoomName : String = ""
    var chatFriendImg : String = ""
    var startX:String = ""
    var startY:String = ""
    var endX:String = ""
    var endY:String = ""
    var selectScheduleNickname : String = ""
    var scheduleEmailPath : String =""
    var kewordName : String = ""

    var chatCount : Int = 0
    var transportTime : Int = -1

    var selectFriendProfile = ProfileDataModel()
    var chatRoomData = mutableListOf<ChatRoomData>()

    var scheduleAlarmTime : AlarmTime = AlarmTime(0,0,0,0,0)
    var scheduleStartAlarmTime : AlarmTime = AlarmTime(0,0,0,0,0)




    val scheduleYYYYMMDD : MutableLiveData<String> = scheduleSetFragmentRepository.scheduleYYYYMMDD//이거 여기 안될거같은데?
























    init {
        fetchPrivacyData()
        fetchProfileData()
        fetchFriendRequestAlarm()
        setFriendList()
    }

    fun bottomNavigationItemSelected(item : MenuItem):Boolean{

        when(item.itemId){
            R.id.navigation_profile->{
                selectFragment.value = "profile"
                return true
            }
            R.id.navigation_friends->{
                selectFragment.value = "friends"
                return true
            }
            R.id.navigation_chat->{
                chatRoomData = mutableListOf()
                selectFragment.value = "chat"
                return true
            }
            R.id.navigation_schedule->{
                selectFragment.value = "schedule"
                return true
            }
        }
        return false
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        when (requestCode) {
            RequestCode.NICKNAME_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        profileNickname.value = it.getStringExtra("nickname")
                        profileStatusMessage.value = it.getStringExtra("statusmessage")
                    }
                }
            }
            RequestCode.ADDRESS_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        profileAddress.value = it.getStringExtra("titleAddress")
                    }
                }
            }
            RequestCode.ACCEPT_FRIEND_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        //fnFriendRequestAlarm()
                        fetchFriendRequestAlarm()
                        setFriendList()
                    }
                }
            }
            RequestCode.CHAT_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        getChatRoomList()
                    }
                }
            }
            RequestCode.MAP_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        meetingPlaceAddress.value = it.getStringExtra("address")
                        endX = it.getStringExtra("endX")!!
                        endY = it.getStringExtra("endY")!!
                        kewordName = it.getStringExtra("keywordName")!!
                    }
                }
            }

        }
    }

    //profile
    fun nickNameEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun profileImgEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun passwordEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun profileAddressEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun logout(){
        auth.signOut()
        logOutSuccess.value = true
    }

    fun changeProfileEditMode(uri: Uri?) {
        if(editProfileData.value == true){
            profileRepository.setProfileImage(uri)
            profileRepository.setProfileData(profileNickname.value, profileStatusMessage.value)
            profileRepository.setPrivacyData(profileAddress.value, profileNickname.value)
        }
        editProfileData.value = editProfileData.value?.not() ?: true
    }

    fun fetchPrivacyData() {
        viewModelScope.launch {
            try {
                val dataMap = profileRepository.getPrivacyData()
                if (dataMap != null) {
                    profileEmail.value = dataMap.get("email") as? String
                    profileName.value = dataMap.get("name") as? String
                    profileNickname.value = dataMap.get("nickname") as? String
                    profilePhone.value = dataMap.get("phoneNumber") as? String
                    profilePassword.value = dataMap.get("password") as? String
                    profileAddress.value = dataMap.get("zonecode") as? String + "," + dataMap.get("address") as? String
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching privacy data", e)
            }
        }
    }

    fun fetchProfileData(){
        viewModelScope.launch {
            try {
                val dataMap = profileRepository.getProfileData()
                if (dataMap != null) {
                    profileImgURL = dataMap.get("imgURL") as String
                    profileStatusMessage.value = dataMap.get("statusmessage") as? String
                    imageUpload.value = true
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error fetching privacy data", e)
            }
        }
    }

    //friend_fragment
    fun fetchFriendRequestAlarm(){
        friendFragmnetRepository.getFriendRequestAlarmData{//알람 이상함
            if(it.exists()){
                friendRequestAlarmStatus.value = true
            }else{
                friendRequestAlarmStatus.value = false
            }
        }
    }

    fun setFriendList(){
        viewModelScope.launch {
            val docRef = firestore.collection("Friend").document(userEmail)
            val friendEmails = friendFragmnetRepository.getFriendListData(docRef)
            val friendsProfileListReturn = friendFragmnetRepository.getFriendProfileData(friendEmails)
            friendsProfileList.value = friendsProfileListReturn
        }
    }

    fun selectFriendAddItem(){
        StartEvent(startFriendAddActivity)
    }

    fun selectFriendAlarmItem(){
        StartEvent(startFriendAlarmActivity)
    }

    fun selectFriend(position:Int){
        selectFriendProfile = friendsProfileList.value!![position]
        StartEvent(startFriendProfileFragment)
    }

    //friendProfile
    fun chatStart(friendEmail: String?){
        viewModelScope.launch {
            val chatInfo = friendProfileFagmentRepository.getChatRoomData(friendEmail,selectFriendProfile.nickname,profileNickname.value)
            chatCount = chatInfo?.chatCount!!
            chatRoomName = chatInfo.chatRoomName
            StartEvent(startChatActivity)
        }
    }

    fun scheduleSetStart(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun friendDelete(){
        val friendEmail = selectFriendProfile.email
        val deleteData = EmailNicknameData(
            userEmail,
            friendEmail,
            profileNickname.value,
            selectFriendProfile.nickname
        )

        friendProfileFagmentRepository.friendDataDelete(deleteData)
        setFriendList()
        StartEvent(friendDeleteSuccess)
    }

    //chatFragment

    fun getChatRoomList(){
        chatFragmentRepository.fetchChatRoomList(friendsProfileList.value!!)
    }

    fun selectChat(position: Int){
        chatRoomFriendNickName = chatRoomProfileList.value!![position].nickname
        chatFriendImg = chatRoomProfileList.value!![position].imgURL//chatProfileList.value!![position]
        val friendEmail = chatRoomProfileList.value!![position].email
        viewModelScope.launch {
            val chatInfo = friendProfileFagmentRepository.getChatRoomData(friendEmail,chatRoomFriendNickName,profileNickname.value)
            chatCount = chatInfo?.chatCount!!
            chatRoomName = chatInfo.chatRoomName
            StartEvent(startChatActivity)
        }
    }

    //ScheduleCalenderFragment

    fun fnScheduleDateSet(date:CalendarDay){
        scheduleSetFragmentRepository.scheduleSet(date)
        StartEvent(startScheduleSetFragment)
        startingPointGet()
    }




    //ScheduleSetFragment


    fun scheduleAmPmSet(bool : Boolean){
        scheduleAmPmSet.value = bool
    }

    fun startScheduleMapActivity(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun startingPointGet(){
        val retrofit = Retrofit.Builder()
            .baseUrl(APIData.NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val NaverGeo = retrofit.create(NaverGeocode::class.java)
        var splitAddress = profileAddress.value!!.split(",")

        val call = NaverGeo.getGeocode(
            APIData.NAVER_CLIENT_ID,
            APIData.NAVER_API_KEY,
            splitAddress[1])

        utils.getRetrofitData(call){
            if(it !=null){
                if(it.addresses.size != 0){
                    startX = it.addresses[0].x
                    startY = it.addresses[0].y
                }
            }
        }
    }

    fun publicTransportTimeGet(){

        if(meetingPlaceAddress.value==""){
            transportCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapTransitApiService = retrofit.create(TMapPublicTransportRoute::class.java)
            val startEndPoint = TransitRouteRequest(
                startX,
                startY,
                endX,
                endY,
                scheduleSetFragmentRepository.dttmSet(scheduleAmPmSet.value,ScheduleTime(scheduleHH.value!!,scheduleMM.value!!)))

            val call = TMapTransitApiService.getPublicTransportRoute(APIData.TMAP_API_KEY,startEndPoint)

            utils.getRetrofitData(call){
                if(it !=null){
                    if(it.metaData == null){
                        transportCheck.value = 5
                    }else{
                        publicTransportTime.value = utils.fnTimeToString(it.metaData.plan.itineraries[0].totalTime)
                        transportTime = it.metaData.plan.itineraries[0].totalTime
                        selectTransport.value = 1
                    }
                }else{
                    transportCheck.value = 4
                }
            }
        }
    }

    fun carTimeGet(){
        if(meetingPlaceAddress.value==""){
            transportCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapCarApiService = retrofit.create(TMapCarRoute::class.java)
            val startEndPoint = CarRouteRequest(startX.toDouble(),
                startY.toDouble(),
                endX.toDouble(),
                endY.toDouble(),
                scheduleSetFragmentRepository.dttmSet(scheduleAmPmSet.value,ScheduleTime(scheduleHH.value!!,scheduleMM.value!!))+"00")

            val call = TMapCarApiService.getCarRoute(APIData.TMAP_API_KEY,"1","CarRouteResponse",startEndPoint)

            utils.getRetrofitData(call){
                if(it !=null){
                    carTime.value = utils.fnTimeToString(it.features[0].properties.totalTime)
                    transportTime = it.features[0].properties.totalTime
                    selectTransport.value = 2
                }else{
                    transportCheck.value = 4
                }
            }
        }
    }

    fun walkingTimeGet(){
        if(meetingPlaceAddress.value==""){
            transportCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapWalkApiService = retrofit.create(TMapWalkRoute::class.java)
            val startEndPoint = WalkRouteRequest(startX.toDouble(),
                startY.toDouble(),
                endX.toDouble(),
                endY.toDouble(),
                "출발점",
                "도착점")
            val call = TMapWalkApiService.getWalkRoute(APIData.TMAP_API_KEY,"1","WalkRouteResponse",startEndPoint)

            utils.getRetrofitData(call){
                if(it != null){
                    walkTime.value = utils.fnTimeToString(it.features[0].properties.totalTime)
                    transportTime = it.features[0].properties.totalTime
                    selectTransport.value = 3
                }else {
                    transportCheck.value = 4
                }
            }
        }
    }

    fun setScheduleRequestData(){
        if(selectTransport.value == 0){
            transportCheck.value = 6
        }else{
            val friendEmail = selectFriendProfile.email
            val childName = userEmailReplace + "||" + friendEmail.replace(".","_")

            val scheduleAlarmTimeData = ScheduleTime(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val scheduleTimeData = ScheduleTime(scheduleHH.value!!,scheduleMM.value!!)
            val alarmTime = utils.fnTimeSet(scheduleAlarmTimeData)
            val alarmTimeSet = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!,scheduleTimeData,scheduleAlarmTimeData,transportTime)
            val currentTime = utils.fnGetCurrentTimeString().substring(0,12)
            val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(alarmTimeSet)

            scheduleStartAlarmTime = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!, scheduleTimeData,
                ScheduleTime("00","00"), transportTime)
            scheduleAlarmTime = alarmTimeSet

            if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
                StartEvent(meetingTimeOver)
            }else{

                val dataMap = mapOf(
                    "email1" to userEmail,
                    "email2" to friendEmail,
                    "nickname1" to profileNickname.value,
                    "nickname2" to selectFriendProfile.nickname,
                    "meetingPlace" to meetingPlaceAddress.value,
                    "meetingPlaceName" to kewordName,
                    "meetingplaceX" to endX,
                    "meetingplaceY" to endY,
                    "meetingTime" to scheduleSetFragmentRepository.dttmSet(scheduleAmPmSet.value, scheduleTimeData),
                    "email1Status" to "consent",
                    "email2Status" to "wait",
                    "email1ProfileImgURl" to profileImgURL,
                    "email2ProfileImgURl" to selectFriendProfile.imgURL,
                    "email1Transport" to selectTransport.value,
                    "email2Transport" to "",
                    "email1Alarm" to alarmTime,
                    "email1StartX" to startX,
                    "email1StartY" to startY,
                    "email2StartX" to "",
                    "email2StartY" to "",
                    "email1TransportTime" to transportTime,
                    "email2TransportTime" to "",
                    "email1StartCheck" to "not",
                    "email2StartCheck" to "not",
                )
                val reference = database.getReference("appointment").child(childName)
                scheduleSetFragmentRepository.sendScheduleRequest(dataMap,reference){
                    if(it){
                        scheduleEmailPath = childName
                        if(alarmTime != "0000"){
                            StartEvent(alarmSet)
                        }
                        StartEvent(appointmentRequestSuccess)
                    }
                }
            }
        }
    }

    fun scheduleSetDataReset(){
        scheduleHH.value = ""
        scheduleMM.value = ""
        scheduleYYYYMMDD.value = ""
        scheduleAmPmSet.value = true
        scheduleAlarmHH.value = ""
        scheduleAlarmMM.value = ""
        selectTransport.value = 0
        transportTime = 0
        meetingPlaceAddress.value = ""
        endX = ""
        endY = ""
        selectScheduleNickname = ""
        scheduleEmailPath = ""
        publicTransportTime.value = ""
        carTime.value = ""
        walkTime.value = ""
        transportCheck.value = 0
    }



















    //scheduleFragment

    var scheduleDeleteText : MutableLiveData<String> = MutableLiveData()
    var scheduleDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData()
    var selectPosition : MutableLiveData<Int> = MutableLiveData(-1)
    var scheduleRefuseOkView : MutableLiveData<Boolean> = MutableLiveData()
    var scheduleListDelete : MutableLiveData<Boolean> = MutableLiveData(false)
    var scheduleAlarmDataList : MutableLiveData<MutableList<ScheduleSet>> = MutableLiveData(mutableListOf())


    val viewRefuseView : MutableLiveData<Boolean> = MutableLiveData(false)
    val viewScheduleDelteView : MutableLiveData<Boolean> = MutableLiveData(false)
    val startScheduleEditFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    val startScheduleAlarmActivity : MutableLiveData<Boolean> = MutableLiveData(false)



    fun fnScheduleClick(position: Int,status: String){
        if(status == "refuse"){
            selectPosition.value = position
            StartEvent(viewRefuseView)
        }else if(status == "consent"||status == "wait"){
            fnSchedulEditDataSet(position)
            StartEvent(startScheduleEditFragment)
        }
    }

    fun fnScheduleLongClick(position: Int){
        scheduleDeleteText.value = scheduleDataList.value!![position].nickname + "님과의 약속을 제거하시겠습니까?"
        StartEvent(viewScheduleDelteView)
        selectPosition.value = position
    }

    fun fnSelectScheduleAlarmItem(){
        StartEvent(startScheduleAlarmActivity)
    }
    fun fnScheduleRefuseOk(data: MutableLiveData<Boolean>){
        val emailPath : String = scheduleDataList.value!![selectPosition.value!!].scheduleName
        val reference = database.getReference("appointment").child(emailPath)

        reference.removeValue().addOnSuccessListener {
            fnScheduleListData()
        }
        StartEvent(data)
    }

    fun fnScheduleListDelete(data : Boolean){
        if(data){
            val emailPath : String = scheduleDataList.value!![selectPosition.value!!].scheduleName
            val reference = database.getReference("appointment").child(emailPath)

            reference.removeValue().addOnSuccessListener {
                fnScheduleListData()
            }
        }
        StartEvent(scheduleListDelete)
    }

    fun fnFRDPathSplit(emailPath:String):Int{
        val emailPathReplace = emailPath?.replace("_",".")
        var splitArray = emailPathReplace?.split("||")
        if(userEmail == splitArray!![0]){
            return 1
        }else if(userEmail == splitArray[1]){
            return 2
        }
        return 0
    }

    fun fnScheduleTimeSplit(date:String){
        val YYYYMMDD = date.substring(0,8)
        val YYYY = date.substring(0,4)
        val MM = date.substring(4,6)
        val DD = date.substring(6,8)
        val hh = date.substring(8,10)
        val mm = date.substring(10,12)

        val date = YYYY+"년 "+MM+"월 "+DD+"일"

        if(hh.toInt()<12){
            scheduleAmPmSet.value = true
            scheduleHH.value = hh

        }else{
            scheduleAmPmSet.value = false
            val pmhh = hh.toInt() - 12
            scheduleHH.value = pmhh.toString()
        }

        scheduleYYYYMMDD.value = YYYYMMDD
        scheduleDate.value = date
        scheduleMM.value = mm
    }

    fun fnScheduleListData(){
        val scheduleList = mutableListOf<ScheduleSet>()
        val scheduleAlarmList = mutableListOf<ScheduleSet>()

        val reference = database.getReference("appointment")

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference){
                        val emailPath = it.key
                        if(fnFRDPathSplit(emailPath!!) == 1){
                            val scheduleData = ScheduleSet(
                                emailPath,
                                it.child("email2").value.toString(),
                                it.child("nickname2").value.toString(),
                                it.child("email2ProfileImgURl").value.toString(),
                                it.child("meetingTime").value.toString(),
                                it.child("meetingPlace").value.toString(),
                                it.child("meetingPlaceName").value.toString(),
                                it.child("email2Status").value.toString(),
                                it.child("meetingplaceX").value.toString(),
                                it.child("meetingplaceY").value.toString(),
                                it.child("email2Alarm").value.toString(),
                                it.child("email2Transport").value.toString(),
                                it.child("email2StartX").value.toString(),
                                it.child("email2StartY").value.toString(),
                                it.child("email2TransportTime").value.toString(),
                                it.child("email1Transport").value.toString(),
                                it.child("email1Alarm").value.toString(),
                            )
                            if(it.child("email1Status").value.toString() == "consent"){
                                scheduleList.add(scheduleData)
                                scheduleDataList.value = scheduleList
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }else if(it.child("email1Status").value.toString() == "wait"){
                                scheduleAlarmList.add(scheduleData)
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }
                        }else if(fnFRDPathSplit(emailPath!!) == 2){
                            val scheduleData = ScheduleSet(
                                emailPath,
                                it.child("email1").value.toString(),
                                it.child("nickname1").value.toString(),
                                it.child("email1ProfileImgURl").value.toString(),
                                it.child("meetingTime").value.toString(),
                                it.child("meetingPlace").value.toString(),
                                it.child("meetingPlaceName").value.toString(),
                                it.child("email1Status").value.toString(),
                                it.child("meetingplaceX").value.toString(),
                                it.child("meetingplaceY").value.toString(),
                                it.child("email1Alarm").value.toString(),
                                it.child("email1Transport").value.toString(),
                                it.child("email1StartX").value.toString(),
                                it.child("email1StartY").value.toString(),
                                it.child("email1TransportTime").value.toString(),
                                it.child("email2Transport").value.toString(),
                                it.child("email2Alarm").value.toString(),
                            )
                            if(it.child("email2Status").value.toString() == "consent"){
                                scheduleList.add(scheduleData)
                                scheduleDataList.value = scheduleList
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }else if(it.child("email2Status").value.toString() == "wait"){
                                scheduleAlarmList.add(scheduleData)
                                scheduleAlarmDataList.value = scheduleAlarmList
                            }
                        }else{
                            scheduleDataList.value = scheduleList
                            scheduleAlarmDataList.value = scheduleAlarmList
                        }
                    }
                } else {
                    scheduleDataList.value = scheduleList
                    scheduleAlarmDataList.value = scheduleAlarmList
                }
            }override fun onCancelled(error: DatabaseError) {
                true
            }
        })
    }

    fun fnSchedulEditDataSet(position: Int){
        val notSplitTime = scheduleDataList.value!![position].time
        val meetingAddress = scheduleDataList.value!![position].meetingPlaceAddress
        val endpointX = scheduleDataList.value!![position].endX
        val endpointY = scheduleDataList.value!![position].endY
        val emailPath = scheduleDataList.value!![position].scheduleName
        val transport = scheduleDataList.value!![position].myTransport
        val alarm = scheduleDataList.value!![position].myAlarmTime

        fnScheduleTimeSplit(notSplitTime)//문제있음//고친건가?
        meetingPlaceAddress.value = meetingAddress
        endX = endpointX
        endY = endpointY
        scheduleEmailPath = emailPath
        selectTransport.value = transport.toInt()
        selectScheduleNickname = scheduleDataList.value!![position].nickname
        scheduleAlarmHH.value = alarm.substring(0,2).toInt().toString()
        scheduleAlarmMM.value = alarm.substring(2,4).toInt().toString()

        when(selectTransport.value){
            1 -> publicTransportTimeGet()
            2 -> carTimeGet()
            3 -> walkingTimeGet()
        }
    }


    //ScheduleEditFragment


    var scheduleEditSuccess : MutableLiveData<Boolean> = MutableLiveData(false)


    fun fnScheduleEditSuccess(){
        val scheduleAlarmTimeData = ScheduleTime(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
        val scheduleTimeData = ScheduleTime(scheduleHH.value!!,scheduleMM.value!!)
        val alarmTimeSet = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!,scheduleTimeData,scheduleAlarmTimeData,transportTime)
        val currentTime = utils.fnGetCurrentTimeString().substring(0,12)
        val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(alarmTimeSet)
        scheduleStartAlarmTime = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!, scheduleTimeData, ScheduleTime("00","00"), transportTime)
        scheduleAlarmTime = alarmTimeSet

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            StartEvent(meetingTimeOver)
        }else{
            val alarmTime = utils.fnTimeSet(scheduleAlarmTimeData)
            val reference = database.getReference("appointment").child(scheduleEmailPath)
            if(fnFRDPathSplit(scheduleEmailPath) == 1){
                reference.child("email1Alarm").setValue(alarmTime)
                reference.child("email1StartX").setValue(startX)
                reference.child("email1StartY").setValue(startY)
                reference.child("email1TransportTime").setValue(transportTime)
                reference.child("email1Transport").setValue(selectTransport.value).addOnSuccessListener {
                    if(alarmTime != "0000"){
                        StartEvent(alarmSet)
                    }
                    StartEvent(scheduleEditSuccess)
                    scheduleSetDataReset()
                }
            }else if(fnFRDPathSplit(scheduleEmailPath) == 2){
                reference.child("email2Alarm").setValue(alarmTime)
                reference.child("email2StartX").setValue(startX)
                reference.child("email2StartY").setValue(startY)
                reference.child("email2TransportTime").setValue(transportTime)
                reference.child("email2Transport").setValue(selectTransport.value)
                if(alarmTime != "0000"){
                    StartEvent(alarmSet)
                }
                StartEvent(scheduleEditSuccess)
                scheduleSetDataReset()
            }
        }
    }

    //ScheduleAlarmActivity

    val startScheduleAcceptFragment : MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnScheduleAcceptClick(position: Int,intent: Intent){
        val notSplitTime = scheduleAlarmDataList.value!![position].time
        val meetingAddress = scheduleAlarmDataList.value!![position].meetingPlaceAddress
        val endpointX = scheduleAlarmDataList.value!![position].endX
        val endpointY = scheduleAlarmDataList.value!![position].endY
        val emailPath = scheduleAlarmDataList.value!![position].scheduleName
        val address = intent.getStringExtra("address")


        fnScheduleTimeSplit(notSplitTime)
        profileAddress.value = address!!
        meetingPlaceAddress.value = meetingAddress
        endX = endpointX
        endY = endpointY
        scheduleEmailPath = emailPath
        selectScheduleNickname = scheduleAlarmDataList.value!![position].nickname//내 닉네임 아님?
        startingPointGet()
        StartEvent(startScheduleAcceptFragment)
    }

    var scheduleAcceptSucess : MutableLiveData<Boolean> = MutableLiveData(false)
    val scheduleRefuse : MutableLiveData<Boolean> = MutableLiveData(false)



    fun fnScheduleAccept(){
        val currentTime = utils.fnGetCurrentTimeString().substring(0,12)

        val scheduleAlarmTimeData = ScheduleTime(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
        val scheduleTimeData = ScheduleTime(scheduleHH.value!!,scheduleMM.value!!)
        val alarmTimeSet = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!,scheduleTimeData,scheduleAlarmTimeData,transportTime)
        val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(alarmTimeSet)
        scheduleAlarmTime = alarmTimeSet
        scheduleStartAlarmTime = scheduleSetFragmentRepository.alarmTimeSet(scheduleAmPmSet.value!!, scheduleTimeData, ScheduleTime("00","00"), transportTime)

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            meetingTimeOver.value = true
            meetingTimeOver.value = false
        }else{
            if(selectTransport.value == 0){
                transportCheck.value = 6
            }else{
                val alarmTime = utils.fnTimeSet(scheduleAlarmTimeData)
                val reference = database.getReference("appointment").child(scheduleEmailPath)
                if(fnFRDPathSplit(scheduleEmailPath) == 1){
                    reference.child("email1Alarm").setValue(alarmTime)
                    reference.child("email1Transport").setValue(selectTransport.value)
                    reference.child("email1StartX").setValue(startX)
                    reference.child("email1StartY").setValue(startY)
                    reference.child("email1TransportTime").setValue(transportTime)
                    reference.child("email1Status").setValue("consent").addOnSuccessListener {
                        if(alarmTime != "0000"){
                            StartEvent(alarmSet)
                        }
                        StartEvent(scheduleAcceptSucess)
                        fnScheduleListData()
                    }
                }else if(fnFRDPathSplit(scheduleEmailPath) == 2){
                    reference.child("email2Alarm").setValue(alarmTime)
                    reference.child("email2Transport").setValue(selectTransport.value)
                    reference.child("email2StartX").setValue(startX)
                    reference.child("email2StartY").setValue(startY)
                    reference.child("email2TransportTime").setValue(transportTime)
                    reference.child("email2Status").setValue("consent").addOnSuccessListener {
                        if(alarmTime != "0000"){
                            StartEvent(alarmSet)
                        }
                        StartEvent(scheduleAcceptSucess)
                        fnScheduleListData()
                    }
                }
            }
        }
    }

    fun fnScheduleRefuse(){

        val reference = database.getReference("appointment").child(scheduleEmailPath)
        if(fnFRDPathSplit(scheduleEmailPath) == 1){
            reference.child("email1Status").setValue("refuse").addOnSuccessListener {
                StartEvent(scheduleRefuse)
                fnScheduleListData()
            }
        }else if(fnFRDPathSplit(scheduleEmailPath) == 2){
            reference.child("email2Status").setValue("refuse").addOnSuccessListener {
                StartEvent(scheduleRefuse)
                fnScheduleListData()
            }
        }
    }
}