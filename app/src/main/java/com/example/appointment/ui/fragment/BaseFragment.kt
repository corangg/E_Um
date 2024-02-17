package com.example.appointment.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.viewbinding.ViewBinding
import com.example.appointment.R
import javax.security.auth.callback.Callback

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

    fun fragmentClose(){
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun getTransaction():FragmentTransaction{
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        return transaction
    }

    fun toast(message : String){
        val inflater = LayoutInflater.from(requireContext())
        val layout = inflater.inflate(R.layout.toast_layout, null)
        val text = layout.findViewById<TextView>(R.id.text)
        text.text = message
        val toast = Toast(requireContext())
        toast.view = layout
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

}