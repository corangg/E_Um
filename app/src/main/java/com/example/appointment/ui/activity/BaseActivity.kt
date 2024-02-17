package com.example.appointment.ui.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.viewbinding.ViewBinding
import com.example.appointment.R


abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResId())
        (binding as ViewDataBinding).lifecycleOwner = this

        initializeUI()
        setObserve()
    }

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    abstract fun setObserve()

    fun toast(message : String){
        val inflater = LayoutInflater.from(this)
        val layout = inflater.inflate(R.layout.toast_layout, null)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message
        val toast = Toast(this)
        toast.view = layout
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }
}