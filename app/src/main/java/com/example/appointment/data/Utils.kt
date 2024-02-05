package com.example.appointment.data

import android.icu.text.SimpleDateFormat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Date
import java.util.Locale

//@Inject constructor()
class Utils {

    companion object{
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val database = FirebaseDatabase.getInstance()
    }

    fun reverseString(data:String,reversePoint:String):String{
        return data.split(reversePoint).reversed().joinToString (reversePoint)
    }

    fun fnGetCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val currentDate = Date(System.currentTimeMillis())
        return dateFormat.format(currentDate)
    }



    /*  interface DataMapCallback {
        fun onDataMapReceived(dataMap: Map<String, Any>?)
    }

    fun readDataFirebase(docRef: DocumentReference, callback: DataMapCallback) {

        docRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val dataMap = documentSnapshot.data
                callback.onDataMapReceived(dataMap as? Map<String, Any>)
            } else {
                callback.onDataMapReceived(null)
            }
        }
    }*///코루틴 안사용한 버전인데 흠... 일단 혹시 모르니 두자
    suspend fun readDataFirebase(docRef: DocumentReference): Map<String, Any>? {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = docRef.get().await()
                if (documentSnapshot.exists()) {
                    documentSnapshot.data
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("Coroutine", "Error fetching document data", e)
                null
            }
        }
    }

    fun readDataFRDAddChildEventListener(reference: DatabaseReference, onChildAddedAction: (DataSnapshot) -> Unit) {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                onChildAddedAction(snapshot)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error reading data", error.toException())
            }
        })
    }

    fun updataDataFireBase(docRef: DocumentReference, field: String, data: String?){
        docRef.update(field,data)
    }

    /*fun readDataFRDaddListenerForSingleValueEvent(reference: DatabaseReference, onChildAddedAction: (DataSnapshot) -> Unit){
        reference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                *//*if(snapshot.exists()){

                }else{

                }*//*
                onChildAddedAction(snapshot)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }*///계속 오류나서 일단 지워둠

    suspend fun updataFRD(reference: DatabaseReference,data: String?){
        try {
            reference.setValue(data).await()
        } catch (e: Exception) {
            Log.e("Coroutine", "Error fetching friend list", e)
        }
    }
}