package com.example.appointment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<B : ViewDataBinding>: Fragment() {

    protected lateinit var binding : B

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId(),container,false)
        (binding as ViewDataBinding).lifecycleOwner = this

        initializeUI()
        setObserve()

        return binding.root
    }

    abstract fun layoutResId(): Int

    abstract fun initializeUI()

    abstract fun setObserve()

}