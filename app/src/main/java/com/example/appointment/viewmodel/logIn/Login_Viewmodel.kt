package com.example.appointment.viewmodel.logIn

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.AddressDBHelper
import com.example.appointment.FirebaseCertified
import com.example.appointment.R
import com.example.appointment.ui.activity.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class Login_Viewmodel(application: Application): AndroidViewModel(application) {

    var showSignup_Fragment:MutableLiveData<Boolean> = MutableLiveData(false)
    var showMainActivity:MutableLiveData<Boolean> = MutableLiveData(false)
    var checkLogin:MutableLiveData<Boolean> = MutableLiveData(false)

    var loginEmail : MutableLiveData<String> = MutableLiveData("")
    var loginPassword : MutableLiveData<String> = MutableLiveData("")

    var loginfail:MutableLiveData<Int> = MutableLiveData(-1)

    var auth:FirebaseAuth = Firebase.auth
    var googleSignInClient : GoogleSignInClient
    var context = getApplication<Application>().applicationContext

    var email: String? = null

    init{
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id)).requestEmail().build()//getString(R.string.default_web_client_id)
        googleSignInClient = GoogleSignIn.getClient(context,gso)
    }
    fun changeSignup(){
        showSignup_Fragment.value = true
    }

    fun login(){
        if (loginEmail.value.toString().isNullOrEmpty() || loginPassword.value.toString().isNullOrEmpty()){
            loginfail.value = 0
        }else{
            auth.signInWithEmailAndPassword(loginEmail.value.toString(),loginPassword.value.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.user?.isEmailVerified == true) {
                        val db =
                            AddressDBHelper(context, loginEmail.value.toString()).writableDatabase
                        showMainActivity.value = true
                    } else {
                        loginfail.value = 1
                    }
                } else {
                    loginfail.value = 2
                }
            }
        }
    }

    fun loginGoogle(view : View){
        (view.context as? LoginActivity)?.googleLoginResult?.launch(googleSignInClient.signInIntent)
    }

    fun firebaseAuthWithGoogle(idToken:String?){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful) {
                if (it.result.user?.isEmailVerified == true) {
                    showMainActivity.value = true

                }
            }
        }
    }

    fun checkAuth(){
        val currentUser = FirebaseCertified.auth.currentUser

        currentUser?.let {
            email = currentUser.email
            if(currentUser.isEmailVerified){
                checkLogin.value=true
            }else{

            }
        }
    }

}
