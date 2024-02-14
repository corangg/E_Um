package com.example.appointment.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.appointment.data.Utils

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val utils = Utils()

    val userEmail = Utils.auth.currentUser?.email ?: ""
    val userEmailReplace = userEmail.replace(".", "_")
}