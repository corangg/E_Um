package com.example.appointment.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

//@Inject constructor()
class Utils {

    companion object{
        val auth = FirebaseAuth.getInstance()
        val firestore = FirebaseFirestore.getInstance()
        val storage = FirebaseStorage.getInstance()
        val database = FirebaseDatabase.getInstance()
    }



    fun splitString(data:String,splitpoint:String):List<String>{
        val splitArray = data.split(splitpoint)

        return splitArray
    }



}