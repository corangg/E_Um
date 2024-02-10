package com.example.appointment.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.icu.text.SimpleDateFormat
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
import com.example.appointment.data.Utils.Companion.storage
import com.example.appointment.model.AlarmTime
import com.example.appointment.model.CarRouteRequest
import com.example.appointment.model.ChatRoomData
import com.example.appointment.model.ProfileDataModel
import com.example.appointment.model.ScheduleSet
import com.example.appointment.model.TransitRouteRequest
import com.example.appointment.model.WalkRouteRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//@HiltViewModel
class MainViewModel /*@Inject constructor*/ (application: Application) : BaseViewModel(application) {

    var selectFragment : MutableLiveData<String> = MutableLiveData("")
    val profileNickname: MutableLiveData<String> = MutableLiveData("")
    val profileStatusMessage: MutableLiveData<String> = MutableLiveData("")
    val profileName: MutableLiveData<String> = MutableLiveData("")
    val profilePhone: MutableLiveData<String> = MutableLiveData("")
    val profileEmail: MutableLiveData<String> = MutableLiveData("")
    val profilePassword: MutableLiveData<String> = MutableLiveData("")
    val profileAddress: MutableLiveData<String> = MutableLiveData("")
    val profileImgURL :MutableLiveData<String> = MutableLiveData("")

    val editProfileData :MutableLiveData<Boolean> = MutableLiveData(false)
    val openGallery :MutableLiveData<Boolean> = MutableLiveData(false)
    val nickNameEditActivityStart:MutableLiveData<Boolean> = MutableLiveData(false)
    val passwordEdit :MutableLiveData<Boolean> = MutableLiveData(false)
    val addressEditActivityStart :MutableLiveData<Boolean> = MutableLiveData(false)
    val logOutSuccess :MutableLiveData<Boolean> = MutableLiveData(false)
    val imageUpload :MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        fnGetPrivacyData()
        fnGetProfileData()
        fnFriendRequestAlarm()
        fnFriendList()
    }

    fun fnBottomNavigationItemSelected(item : MenuItem):Boolean{

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


    fun fnHandleActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
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
                        fnFriendRequestAlarm()
                        fnFriendList()
                    }
                }
            }
            RequestCode.CHAT_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        fnChatRoomList()
                    }
                }
            }
            RequestCode.MAP_REQUEST_CODE->{
                if(resultCode == Activity.RESULT_OK){
                    data?.let {
                        meetingPlaceAddress.value = it.getStringExtra("address")
                        endX.value = it.getStringExtra("endX")
                        endY.value = it.getStringExtra("endY")
                        kewordName.value = it.getStringExtra("keywordName")
                    }
                }
            }

        }
    }


    //profile
    fun fnProfileAddressEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnNickNameEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnPasswordEdit(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }


    fun fnOpenGallery(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnLogout(){
        auth.signOut()

        logOutSuccess.value = true
    }

    fun fnProfileEditMode(uri: Uri?) {
        if(editProfileData.value == true){
            fnProfileImageUpdate(uri)
            fnProfileEdit()
            fnPrivacyEdit()
        }
        editProfileData.value = editProfileData.value?.not() ?: true
    }

    fun fnGetPrivacyData() {
        val docRef = firestore.collection("Privacy").document(userEmail)

        viewModelScope.launch {
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null){
                    profileEmail.value = dataMap?.get("email") as? String
                    profileName.value = dataMap?.get("name") as? String
                    profileNickname.value = dataMap?.get("nickname") as? String
                    profilePhone.value = dataMap?.get("phoneNumber") as? String
                    profilePassword.value = dataMap?.get("password") as? String
                    profileAddress.value = dataMap?.get("zonecode") as? String + "," + dataMap?.get("address") as? String
                }
            }catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend list", e)
            }
        }
    }

    fun fnGetProfileData(){
        val docRef = firestore.collection("Profile").document(userEmail)

        viewModelScope.launch {
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null){
                    profileImgURL.value = dataMap?.get("imgURL") as? String
                    profileStatusMessage.value = dataMap?.get("statusmessage") as? String
                    imageUpload.value = true
                }
            }catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend list", e)
            }
        }
    }

    fun fnProfileEdit(){
        val docRef = firestore.collection("Profile").document(userEmail)

        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"statusmessage",profileStatusMessage.value)

    }

    fun fnPrivacyEdit(){
        val splitAddress = profileAddress.value?.split(",")
        val userEmail = auth.currentUser!!.email
        val docRef = firestore.collection("Privacy").document(userEmail!!)

        utils.updataDataFireBase(docRef,"zonecode",splitAddress!![0])
        utils.updataDataFireBase(docRef,"nickname",profileNickname.value)
        utils.updataDataFireBase(docRef,"address",splitAddress!![1])
    }

    fun fnProfileImageUpdate(photoUri: Uri?){
        if(photoUri!=null){
            var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(java.util.Date())
            var storagePath = storage.reference.child("userProfile").child(auth.currentUser?.email.toString()).child("profileimg")

            val userEmail = auth.currentUser!!.email
            val docRef = firestore.collection("Profile").document(userEmail!!)


            storagePath.putFile(photoUri!!).continueWithTask{
                return@continueWithTask storagePath.downloadUrl
            }.addOnCompleteListener{downloadUrl->
                viewModelScope.launch {
                    utils.updataDataFireBase(docRef,"imgURL",downloadUrl.result.toString())
                    utils.updataDataFireBase(docRef,"timestamp",timestamp)
                }
            }
        }
    }


    //friend_fragment

    val friendRequestAlarmStatus : MutableLiveData<Boolean> = MutableLiveData()
    val friendsProfileList: MutableLiveData<MutableList<ProfileDataModel>> = MutableLiveData()
    val selectFriendProfile : MutableLiveData<ProfileDataModel> = MutableLiveData()

    val startFriendAddActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendAlarmActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    val startFriendProfileFragment : MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnFriendRequestAlarm() {
        //val emailReplace = userEmail.replace(".", "_")
        val reference = database.getReference(userEmailReplace).child("friendRequest")

        reference.addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    friendRequestAlarmStatus.value = true

                } else {
                    friendRequestAlarmStatus.value = false
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun fnFriendList(){
        viewModelScope.launch {
            val docRef = firestore.collection("Friend").document(userEmail)

            val friendEmails = fetchFriendList(docRef)
            val friendsProfileListReturn = fetchFriendProfiles(friendEmails)
            friendsProfileList.value = friendsProfileListReturn
        }
    }

    suspend fun fetchFriendList(docRef : DocumentReference): List<String> {
        val friendList = mutableListOf<String>()

        try {
            val dataMap = utils.readDataFirebase(docRef)
            if (dataMap != null) {
                for ((_, value) in dataMap) {
                    friendList.add(value.toString())
                }
            }
        } catch (e: Exception) {
            Log.e("Coroutine", "Error fetching friend list", e)
        }

        return friendList
    }

    suspend fun fetchFriendProfiles(friendEmails: List<String>): MutableList<ProfileDataModel> {
        val profileList = mutableListOf<ProfileDataModel>()

        for (friendEmail in friendEmails) {
            val docRef = firestore.collection("Profile").document(friendEmail)
            try {
                val dataMap = utils.readDataFirebase(docRef)
                if (dataMap != null) {
                    val profileData = ProfileDataModel(
                        dataMap["email"] as String,
                        dataMap["nickname"] as String,
                        dataMap["statusmessage"] as String,
                        "",
                        dataMap["imgURL"] as String
                    )
                    profileList.add(profileData)
                }
            } catch (e: Exception) {
                Log.e("Coroutine", "Error fetching friend profile", e)
            }
        }
        return profileList
    }

    fun fnSelectFriendAddItem(){
        StartEvent(startFriendAddActivity)
    }

    fun fnSelectFriendAlarmItem(){
        StartEvent(startFriendAlarmActivity)
    }

    fun fnSelectFriend(position:Int){
        selectFriendProfile.value = friendsProfileList.value!![position]
        StartEvent(startFriendProfileFragment)
    }


    //friendProfile
    var chatCount : MutableLiveData<Int?> = MutableLiveData(0)
    var chatRoomName:MutableLiveData<String?> = MutableLiveData("")
    var startChatActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    var startScheduleCalendarFragment : MutableLiveData<Boolean> = MutableLiveData(false)
    var friendDeleteSuccess:MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnChatStart(friendEmail: String?){
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val reference = database.getReference("chat")
        val chatName = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = utils.reverseString(chatName,"||")

        reference.child(chatName).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    chatCount.value = snapshot.childrenCount.toInt()
                    chatRoomName.value=chatName
                    StartEvent(startChatActivity)
                } else {
                    reference.child(reversedchatName).addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                chatCount.value = snapshot.childrenCount.toInt()
                                chatRoomName.value = reversedchatName
                                StartEvent(startChatActivity)
                            } else {
                                val chatPath = reference.child(reversedchatName).child(utils.fnGetCurrentTimeString())
                                val dataMap = mapOf(
                                    "email1" to userEmail,
                                    "email2" to friendEmail,
                                    "nickname1" to profileNickname.value,
                                    "nickname2" to selectFriendProfile.value!!.nickname,
                                    "email1MessageCheck" to "0",
                                    "email2MessageCheck" to "0"
                                )
                                chatPath.setValue(dataMap).addOnSuccessListener {
                                    chatCount.value = snapshot.childrenCount.toInt()
                                    chatRoomName.value = reversedchatName
                                    StartEvent(startChatActivity)
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Log.d("ChatStartError","error",error.toException())
                        }
                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("ChatStartError","error",error.toException())
            }
        })
    }

    fun fnStartFragmentScheduleSet(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnFriendDelete(){
        val docRef = firestore.collection("Friend")
        val updates = HashMap<String, Any>()
        val friendEmail = selectFriendProfile.value!!.email
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val chatName : String = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = utils.reverseString(chatName,"||")
        val chatReference = database.getReference("chat")
        val appointmentReference = database.getReference("appointment")

        updates[selectFriendProfile.value!!.nickname] = FieldValue.delete()
        docRef.document(userEmail).update(updates)

        updates[profileNickname.value!!] = FieldValue.delete()
        docRef.document(friendEmail).update(updates)

        chatReference.child(chatName).removeValue().addOnSuccessListener {
            appointmentReference.child(chatName).removeValue()
        }
        chatReference.child(reversedchatName).removeValue().addOnSuccessListener {
            appointmentReference.child(reversedchatName).removeValue().addOnSuccessListener {
            }
        }
        fnFriendList()
        StartEvent(friendDeleteSuccess)
    }


    //chatFragment
    val chatRoomProfileList:MutableLiveData<MutableList<ChatRoomData>> = MutableLiveData()
    var chatRoomData = mutableListOf<ChatRoomData>()
    val chatRoomFriendNickName: MutableLiveData<String> = MutableLiveData("")
    val chatFriendImg: MutableLiveData<String> = MutableLiveData("")

    fun fnChatRoomList(){
        val reference = database.getReference("chat")
        var chatRoomSize : Int = 0

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    utils.readDataFRDAddChildEventListener(reference){
                        val emailPath = it.key?: ""
                        val replaceEmailPath = emailPath.replace("_",".")
                        val splitArray = replaceEmailPath.split("||")
                        val totalChatCount = it.childrenCount.toInt()

                        if(chatRoomSize == 0){
                            chatRoomProfileList.value = mutableListOf()
                        }//원래는 밑이었는데 밑이면 아예 의미가 없을꺼 같은데? 이상하면 확인해 보자

                        if(userEmail == splitArray[0]){
                            chatProfile(emailPath,totalChatCount)
                            chatRoomSize = chatRoomSize + 1

                        }else if(userEmail == splitArray[1]){
                            chatProfile(emailPath,totalChatCount)
                            chatRoomSize = chatRoomSize + 1
                        }
                    }
                } else {
                    chatRoomProfileList.value = mutableListOf()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun chatProfile(emailPath:String?,totalChatCount:Int){
        val reference = database.getReference("chat").child(emailPath!!)
        chatRoomData = mutableListOf()

        /*var a:Int =  0
        utils.readDataFRDAddChildEventListener(reference){
            a = a+1

            if(a>totalChatCount){
                asf(reference,a)


                *//*val at = it.child("message").value.toString()


                at*//*
            }

            true
        }*/
        //채팅 오면 바로 리셋되게 하고 싶은데 어떻게 해야될지 고민임 폰이 하나라 확인이 어려움 쳇엑티 처럼 하면 되나?

        fnLastChatSet(reference,totalChatCount)
    }

    fun fnLastChatSet(reference:DatabaseReference,totalChatCount:Int){
        reference.limitToFirst(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                    val firstemail = snapshot.children.first().child("email1").value.toString()
                    val secondeemail = snapshot.children.first().child("email2").value.toString()
                    val firstnickname = snapshot.children.first().child("nickname1").value.toString()
                    val secondenickname = snapshot.children.first().child("nickname2").value.toString()
                    val emil1CheckCount = snapshot.children.first().child("email1MessageCheck").value.toString().toInt()
                    val emil2CheckCount = snapshot.children.first().child("email2MessageCheck").value.toString().toInt()

                    if(firstemail == userEmail){
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val notCheckChatCount = totalChatCount - emil2CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,secondeemail,secondenickname)
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }else{
                        reference.limitToLast(1).addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val notCheckChatCount : Int = totalChatCount - emil1CheckCount - 1

                                fnChatRoomDataAdd(snapshot,notCheckChatCount,firstemail,firstnickname)
                            }

                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun fnChatRoomDataAdd(snapshot: DataSnapshot, notCheckChatCount: Int, email: String, nickname: String){
        val lastChatTime = snapshot.children.last().key?.toLong()
        val lastMessage = snapshot.children.last().child("message").value.toString()

        var imgURL :String = ""

        for (i in 0..friendsProfileList.value!!.size-1)
        {
            if(nickname==friendsProfileList.value!![i].nickname){
                imgURL = friendsProfileList.value!![i].imgURL
            }
        }

        val chatRoomProfile = ChatRoomData(
            email,
            nickname,
            lastMessage,
            lastChatTime,
            notCheckChatCount,
            imgURL
        )
        chatRoomData.add(chatRoomProfile)
        fnChatProfileArray()
    }

    fun fnChatProfileArray(){
        val sortedList = chatRoomData.sortedByDescending { it.time }
        chatRoomProfileList.value = sortedList.toMutableList()
    }

    fun fnSelectChat(position: Int){
        chatRoomFriendNickName.value = chatRoomProfileList.value!![position].nickname
        chatFriendImg.value = chatRoomProfileList.value!![position].imgURL//chatProfileList.value!![position]
        fnChatStart(chatRoomProfileList.value!![position].email)
    }

    //ScheduleCalenderFragment

    val scheduleDate : MutableLiveData<String> = MutableLiveData("")
    val scheduleYYYYMMDD : MutableLiveData<String> = MutableLiveData("")
    val startScheduleSetFragment : MutableLiveData<Boolean> = MutableLiveData(false)

    fun fnScheduleDateSet(date:CalendarDay){
        val year = date.year
        val month = date.month
        val day = date.day

        var monthString = "00"
        var dayString = "00"

        if(month<10){
            monthString = "0"+month.toString()
        }else{
            monthString = month.toString()
        }
        if(day<10){
            dayString = "0"+day.toString()
        }else{
            dayString = day.toString()
        }

        scheduleDate.value = year.toString() + "년 "+ monthString + "월 " + dayString + "일"
        scheduleYYYYMMDD.value = year.toString() + monthString + dayString

        StartEvent(startScheduleSetFragment)
        fnStartingPointSet()
    }




    //ScheduleSetFragment

    val alarmSet : MutableLiveData<Boolean> = MutableLiveData(false)
    val scheduleAmPmSet : MutableLiveData<Boolean> = MutableLiveData(true)
    var scheduleHH : MutableLiveData<String> = MutableLiveData("")
    var scheduleMM : MutableLiveData<String> = MutableLiveData("")
    var scheduleAlarmHH : MutableLiveData<String> = MutableLiveData("")
    var scheduleAlarmMM : MutableLiveData<String> = MutableLiveData("")
    var meetingPlaceAddress :MutableLiveData<String> = MutableLiveData("")
    var startScheduleMapActivity : MutableLiveData<Boolean> = MutableLiveData(false)
    var publicTransportTime :MutableLiveData<String> = MutableLiveData("")
    var carTime :MutableLiveData<String> = MutableLiveData("")
    var walkTime : MutableLiveData<String> = MutableLiveData("")
    var transportCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var carCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var walkCheck : MutableLiveData<Int> = MutableLiveData(-1)
    var startX:MutableLiveData<String> = MutableLiveData("")
    var startY:MutableLiveData<String> = MutableLiveData("")
    var endX:MutableLiveData<String> = MutableLiveData("")
    var endY:MutableLiveData<String> = MutableLiveData("")
    var transportTime : MutableLiveData<Int> = MutableLiveData()
    var appointmentRequestSuccess : MutableLiveData<Boolean> = MutableLiveData()
    var selectTransport : MutableLiveData<Int> = MutableLiveData(0)
    var selectScheduleNickname : MutableLiveData<String> = MutableLiveData()
    var scheduleEmailPath : MutableLiveData<String> = MutableLiveData()
    var meetingTimeOver : MutableLiveData<Boolean> = MutableLiveData()
    var kewordName : MutableLiveData<String?> = MutableLiveData("")
    var startCheckAlarmTime : MutableLiveData<Int> = MutableLiveData(5)

    fun fnScheduleAmPmSet(bool : Boolean){
        scheduleAmPmSet.value = bool
    }

    fun fnStartScheduleMapActivity(data: MutableLiveData<Boolean>){
        StartEvent(data)
    }

    fun fnDttmSet():String{
        var hourint = 0

        if(scheduleAmPmSet.value == true){
            if(scheduleHH.value == ""){
                hourint = 0
            }else{
                hourint = scheduleHH.value!!.toInt()
            }
        }else{
            if(scheduleHH.value == ""){
                hourint = 12
            }else{
                hourint = scheduleHH.value!!.toInt() + 12
            }
        }

        val Dttm :String= scheduleYYYYMMDD.value!! + utils.fnTimeSet(hourint.toString(),scheduleMM.value!!)
        return Dttm
    }

    fun fnAlarmTimeSet(alarmhhString: String,alarmmmString: String): AlarmTime {
        var alarmTime : AlarmTime

        var YYYY : Int = scheduleYYYYMMDD.value!!.substring(0,4).toInt()
        var MM : Int = scheduleYYYYMMDD.value!!.substring(4,6).toInt()
        var DD : Int = scheduleYYYYMMDD.value!!.substring(6,8).toInt()
        var hh : Int = 0
        var mm : Int = 0
        val transportMin = transportTime.value!!.div(60)
        var alarmhh : Int = 0
        var alarmmm : Int = 0

        if(scheduleHH.value != ""){
            if(scheduleAmPmSet.value == true){
                hh = scheduleHH.value!!.toInt()
            }else{
                hh = scheduleHH.value!!.toInt() + 12
            }
        }
        if(scheduleMM.value != ""){
            mm = scheduleMM.value!!.toInt()
        }

        if (alarmhhString != ""){
            alarmhh = alarmhhString.toInt()
        }

        if (alarmmmString != ""){
            alarmmm = alarmmmString.toInt()
        }


        mm = mm - transportMin.rem(60) - alarmmm
        if(mm < 0){
            mm = mm + 60
            hh = hh - 1
            if(mm<0){
                mm = mm + 60
                hh = hh - 1
            }
        }

        hh = hh - transportMin.div(60) - alarmhh

        if(hh < 0){
            hh = hh + 24
            DD = DD -1
        }

        if(DD < 1){
            MM = MM - 1
            when(MM){
                0,1,3,5,7,8,10,12 -> DD = DD + 31
                4,6,9,11 -> DD = DD + 30
                2->{
                    if(YYYY.rem(4)==0){
                        DD = DD + 29
                    }else{
                        DD = DD + 28
                    }
                }
            }
        }

        if(MM < 1){
            MM = MM + 12
            YYYY = YYYY - 1
        }

        alarmTime = AlarmTime(YYYY,MM,DD, hh, mm)

        return alarmTime
    }

    fun fnStartingPointSet(){
        val retrofit = Retrofit.Builder()
            .baseUrl(APIData.NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val NaverGeo = retrofit.create(NaverGeocode::class.java)
        var splitAddress = profileAddress.value.toString().split(",")

        val call = NaverGeo.getGeocode(
            APIData.NAVER_CLIENT_ID,
            APIData.NAVER_API_KEY,
            splitAddress[1])


        utils.getRetrofitData(call){
            if(it !=null){
                if(it.addresses.size != 0){
                    startX.value = it.addresses[0].x
                    startY.value = it.addresses[0].y
                }
            }
        }
    }
    fun fnPublicTransportTimeSet(){
       if(meetingPlaceAddress.value==""){
            transportCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

           val TMapTransitApiService = retrofit.create(TMapPublicTransportRoute::class.java)
           val startEndPoint = TransitRouteRequest(
                startX.value!!,
                startY.value!!,
                endX.value!!,
                endY.value!!,
                fnDttmSet())

           val call = TMapTransitApiService.getPublicTransportRoute(APIData.TMAP_API_KEY,startEndPoint)

           utils.getRetrofitData(call){
               if(it !=null){
                   if(it.metaData == null){
                       transportCheck.value = 5
                   }else{
                       publicTransportTime.value = utils.fnTimeToString(it.metaData.plan.itineraries[0].totalTime)
                       transportTime.value = it.metaData.plan.itineraries[0].totalTime
                       selectTransport.value = 1
                   }
               }else{
                   transportCheck.value = 4
               }
           }
        }
    }

    fun fnCarTimeSet(){
        if(meetingPlaceAddress.value==""){
            carCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapCarApiService = retrofit.create(TMapCarRoute::class.java)
            val startEndPoint = CarRouteRequest(startX.value!!.toDouble(),
                startY.value!!.toDouble(),
                endX.value!!.toDouble(),
                endY.value!!.toDouble(),
                fnDttmSet()+"00")

            val call = TMapCarApiService.getCarRoute(APIData.TMAP_API_KEY,"1","CarRouteResponse",startEndPoint)

            utils.getRetrofitData(call){
                if(it !=null){
                    carTime.value = utils.fnTimeToString(it.features[0].properties.totalTime)
                    transportTime.value = it.features[0].properties.totalTime
                    selectTransport.value = 2
                }else{
                    carCheck.value = 4
                }
            }
        }
    }

    fun fnWalkingTimeSet(){
        if(meetingPlaceAddress.value==""){
            walkCheck.value = 3
        }else{
            val retrofit = Retrofit.Builder()
                .baseUrl(APIData.TMAP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val TMapWalkApiService = retrofit.create(TMapWalkRoute::class.java)
            val startEndPoint = WalkRouteRequest(startX.value!!.toDouble(),
                startY.value!!.toDouble(),
                endX.value!!.toDouble(),
                endY.value!!.toDouble(),
                "출발점",
                "도착점")
            val call = TMapWalkApiService.getWalkRoute(APIData.TMAP_API_KEY,"1","WalkRouteResponse",startEndPoint)

            utils.getRetrofitData(call){
                if(it != null){
                    walkTime.value = utils.fnTimeToString(it.features[0].properties.totalTime)
                    transportTime.value = it.features[0].properties.totalTime
                    selectTransport.value = 3
                }else {
                    walkCheck.value = 4
                }
            }
        }
    }

    fun fnAppointmentRequest(){
        if(selectTransport.value == 0){
            transportCheck.value = 6
        }else{
            val userEmailReplace = userEmail.replace(".","_")
            val friendEmail = selectFriendProfile.value!!.email
            val friendEmailReplace = friendEmail.replace(".","_")
            val childName = userEmailReplace+"||"+friendEmailReplace
            val alarmTime = utils.fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val alarmTimeSet = fnAlarmTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val currentTime = utils.fnGetCurrentTimeString().substring(0,12)
            val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(alarmTimeSet)

            if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
                StartEvent(meetingTimeOver)
            }else{

                val dataMap = mapOf(
                    "email1" to userEmail,
                    "email2" to friendEmail,
                    "nickname1" to profileNickname.value,
                    "nickname2" to selectFriendProfile.value!!.nickname,
                    "meetingPlace" to meetingPlaceAddress.value,
                    "meetingPlaceName" to kewordName.value,
                    "meetingplaceX" to endX.value,
                    "meetingplaceY" to endY.value,
                    "meetingTime" to fnDttmSet(),
                    "email1Status" to "consent",
                    "email2Status" to "wait",
                    "email1ProfileImgURl" to profileImgURL.value,
                    "email2ProfileImgURl" to selectFriendProfile.value!!.imgURL,
                    "email1Transport" to selectTransport.value,
                    "email2Transport" to "",
                    "email1Alarm" to alarmTime,
                    "email1StartX" to startX.value,
                    "email1StartY" to startY.value,
                    "email2StartX" to "",
                    "email2StartY" to "",
                    "email1TransportTime" to transportTime.value,
                    "email2TransportTime" to "",
                    "email1StartCheck" to "not",
                    "email2StartCheck" to "not",
                )

                val reference = database.getReference("appointment").child(childName)
                reference.setValue(dataMap).addOnSuccessListener{
                    scheduleEmailPath.value = childName
                    if(alarmTime != "0000"){
                        StartEvent(alarmSet)
                    }
                    StartEvent(appointmentRequestSuccess)
                }
            }
        }
    }

    fun fnScheduleSetDataReset(){
        scheduleHH.value = ""
        scheduleMM.value = ""
        scheduleYYYYMMDD.value = ""
        scheduleAmPmSet.value = true
        scheduleAlarmHH.value = ""
        scheduleAlarmMM.value = ""
        selectTransport.value = 0
        transportTime.value = 0
        meetingPlaceAddress.value = ""
        endX.value = ""
        endY.value = ""
        selectScheduleNickname.value = ""
        scheduleEmailPath.value = ""
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
        endX.value = endpointX
        endY.value = endpointY
        scheduleEmailPath.value = emailPath
        selectTransport.value = transport.toInt()
        selectScheduleNickname.value = scheduleDataList.value!![position].nickname
        scheduleAlarmHH.value = alarm.substring(0,2).toInt().toString()
        scheduleAlarmMM.value = alarm.substring(2,4).toInt().toString()

        when(selectTransport.value){
            1 -> fnPublicTransportTimeSet()
            2 -> fnCarTimeSet()
            3 -> fnWalkingTimeSet()
        }
    }


    //ScheduleEditFragment


    var scheduleEditSuccess : MutableLiveData<Boolean> = MutableLiveData(false)


    fun fnScheduleEditSuccess(){
        val currentTime = utils.fnGetCurrentTimeString().substring(0,12)

        val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(fnAlarmTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!))

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            StartEvent(meetingTimeOver)
        }else{
            val emailPath = scheduleEmailPath.value
            val alarmTime = utils.fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
            val reference = database.getReference("appointment").child(emailPath!!)
            if(fnFRDPathSplit(emailPath!!) == 1){
                reference.child("email1Alarm").setValue(alarmTime)
                reference.child("email1StartX").setValue(startX.value)
                reference.child("email1StartY").setValue(startY.value)
                reference.child("email1TransportTime").setValue(transportTime.value)
                reference.child("email1Transport").setValue(selectTransport.value).addOnSuccessListener {
                    if(alarmTime != "0000"){
                        StartEvent(alarmSet)
                    }
                    StartEvent(scheduleEditSuccess)
                    fnScheduleSetDataReset()
                }
            }else if(fnFRDPathSplit(emailPath!!) == 2){
                reference.child("email2Alarm").setValue(alarmTime)
                reference.child("email2StartX").setValue(startX.value)
                reference.child("email2StartY").setValue(startY.value)
                reference.child("email2TransportTime").setValue(transportTime.value)
                reference.child("email2Transport").setValue(selectTransport.value)
                if(alarmTime != "0000"){
                    StartEvent(alarmSet)
                }
                StartEvent(scheduleEditSuccess)
                fnScheduleSetDataReset()
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
        endX.value = endpointX
        endY.value = endpointY
        scheduleEmailPath.value = emailPath
        selectScheduleNickname.value = scheduleAlarmDataList.value!![position].nickname//내 닉네임 아님?
        fnStartingPointSet()
        StartEvent(startScheduleAcceptFragment)
    }

    var scheduleAcceptSucess : MutableLiveData<Boolean> = MutableLiveData(false)
    val scheduleRefuse : MutableLiveData<Boolean> = MutableLiveData(false)



    fun fnScheduleAccept(){
        val currentTime = utils.fnGetCurrentTimeString().substring(0,12)

        val alarmYYYYMMDDhhmm = utils.fnAlarmYYYYMMDDhhmm(fnAlarmTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!))

        if(currentTime.toLong() > alarmYYYYMMDDhhmm.toLong()){
            meetingTimeOver.value = true
            meetingTimeOver.value = false
        }else{
            if(selectTransport.value == 0){
                transportCheck.value = 6
            }else{
                val emailPath = scheduleEmailPath.value
                val alarmTime = utils.fnTimeSet(scheduleAlarmHH.value!!,scheduleAlarmMM.value!!)
                val reference = database.getReference("appointment").child(emailPath!!)
                if(fnFRDPathSplit(emailPath!!) == 1){
                    reference.child("email1Alarm").setValue(alarmTime)
                    reference.child("email1Transport").setValue(selectTransport.value)
                    reference.child("email1StartX").setValue(startX.value)
                    reference.child("email1StartY").setValue(startY.value)
                    reference.child("email1TransportTime").setValue(transportTime.value)
                    reference.child("email1Status").setValue("consent").addOnSuccessListener {
                        if(alarmTime != "0000"){
                            StartEvent(alarmSet)
                        }
                        StartEvent(scheduleAcceptSucess)
                        fnScheduleListData()
                    }
                }else if(fnFRDPathSplit(emailPath!!) == 2){
                    reference.child("email2Alarm").setValue(alarmTime)
                    reference.child("email2Transport").setValue(selectTransport.value)
                    reference.child("email2StartX").setValue(startX.value)
                    reference.child("email2StartY").setValue(startY.value)
                    reference.child("email2TransportTime").setValue(transportTime.value)
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

        val emailPath = scheduleEmailPath.value
        val reference = database.getReference("appointment").child(emailPath!!)
        if(fnFRDPathSplit(emailPath!!) == 1){
            reference.child("email1Status").setValue("refuse").addOnSuccessListener {
                StartEvent(scheduleRefuse)
                fnScheduleListData()
            }
        }else if(fnFRDPathSplit(emailPath!!) == 2){
            reference.child("email2Status").setValue("refuse").addOnSuccessListener {
                StartEvent(scheduleRefuse)
                fnScheduleListData()
            }
        }
    }
}