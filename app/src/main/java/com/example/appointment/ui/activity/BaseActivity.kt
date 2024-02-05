package com.example.appointment.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.AndroidViewModel
import androidx.viewbinding.ViewBinding


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
}