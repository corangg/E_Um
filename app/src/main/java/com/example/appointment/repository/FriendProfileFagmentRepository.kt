package com.example.appointment.repository

import android.util.Log
import com.example.appointment.data.Utils
import com.example.appointment.data.ChatInfo
import com.example.appointment.data.EmailNicknameData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

class FriendProfileFagmentRepository(private val utils: Utils) {
    suspend fun getChatRoomData(friendEmail: String?, friendNickname: String?, profileNickname: String?): ChatInfo? {
        val userEmail = Utils.auth.currentUser?.email
        val userEmailReplace = userEmail?.replace(".", "_")
        val friendEmailReplace = friendEmail!!.replace(".", "_")
        val reference = Utils.database.getReference("chat")
        val chatName = userEmailReplace + "||" + friendEmailReplace
        val reversedchatName = utils.reverseString(chatName, "||")

        val snapshot = reference.child(chatName).get().await()
        return if (snapshot.exists()) {
            ChatInfo(snapshot.childrenCount.toInt(), chatName)
        } else {
            val reversedSnapshot = reference.child(reversedchatName).get().await()
            if (reversedSnapshot.exists()) {
                ChatInfo(reversedSnapshot.childrenCount.toInt(), reversedchatName)
            } else {
                val chatPath = reference.child(reversedchatName).child(utils.fnGetCurrentTimeString())
                val createData = EmailNicknameData(
                    friendEmail,
                    userEmail,
                    friendNickname,
                    profileNickname
                )

                val success = createChatRoom(chatPath, createData)
                if (success) {
                    ChatInfo(reversedSnapshot.childrenCount.toInt(), reversedchatName)
                } else {
                    null
                }
            }
        }
    }

    private suspend fun createChatRoom(reference: DatabaseReference, createData: EmailNicknameData): Boolean {
        return try {
            val dataMap = mapOf(
                "email1" to createData.email1,
                "email2" to createData.email2,
                "nickname1" to createData.nickname1,
                "nickname2" to createData.nickname2,
                "email1MessageCheck" to "0",
                "email2MessageCheck" to "0"
            )
            reference.setValue(dataMap).await()
            true
        } catch (e: Exception) {
            Log.e("createChatRoom", "Error creating chat room", e)
            false
        }
    }

    fun friendDataDelete(emailNicknameData: EmailNicknameData){

        val docRef = Utils.firestore.collection("Friend")
        val chatName : String = emailNicknameData.email1!!.replace(".","_") + "||" + emailNicknameData.email2!!.replace(".","_")
        val reversedchatName = utils.reverseString(chatName,"||")
        val chatReference = Utils.database.getReference("chat")
        val appointmentReference = Utils.database.getReference("appointment")
        val updates = HashMap<String, Any>()

        updates[emailNicknameData.nickname2!!] = FieldValue.delete()
        docRef.document(emailNicknameData.email1).update(updates)

        updates[emailNicknameData.nickname1!!] = FieldValue.delete()
        docRef.document(emailNicknameData.email2).update(updates)

        chatReference.child(chatName).removeValue().addOnSuccessListener {
            appointmentReference.child(chatName).removeValue()
        }
        chatReference.child(reversedchatName).removeValue().addOnSuccessListener {
            appointmentReference.child(reversedchatName).removeValue()
        }
    }
}