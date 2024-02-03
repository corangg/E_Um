package com.example.appointment.ui.fragment.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.appointment.R
import com.example.appointment.ui.activity.profile.AddressEditActivity
import com.example.appointment.ui.activity.profile.NickNameActivity
import com.example.appointment.ui.activity.profile.PasswordEditActivity
import com.example.appointment.databinding.FragmentProfileBinding
import com.example.appointment.ui.activity.login.LoginActivity
import com.example.appointment.ui.fragment.BaseFragment
import com.example.appointment.viewmodel.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import kotlin.text.Typography.dagger

//@AndroidEntryPoint@Inject constructor()
class Profile_Fragment : BaseFragment<FragmentProfileBinding>() {
    companion object{
        const val NICKNAME_REQUEST_CODE =2911
        const val ADDRESS_REQUEST_CODE =2912
    }

    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var mActivity: AppCompatActivity
    private var photoUri : Uri? = null
    private var photoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == AppCompatActivity.RESULT_OK){
            photoUri = it.data?.data
            binding.imgProfile.setImageURI(photoUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_profile_
    }

    override fun initializeUI() {
        binding.viewmodel = profileViewModel

        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        binding.imgProfile.isEnabled=false
        binding.layoutNickname.isEnabled=false
        binding.layoutPassword.isEnabled = false
        binding.layoutAddress.isEnabled = false

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_profile_edit -> {
                    profileViewModel.profileEditMode(photoUri)
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is AppCompatActivity) {
            mActivity = context
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        profileViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    override fun setObserve(){
        profileViewModel.addressEditActivityStart.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent = Intent(requireActivity(), AddressEditActivity::class.java)
                intent.putExtra("email",activity?.intent?.getStringExtra("email"))
                intent.putExtra("mainaddress",profileViewModel. profileAddress.value)

                startActivityForResult(intent, ADDRESS_REQUEST_CODE)
            }
        }

        profileViewModel.passwordEdit.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent = Intent(requireActivity(), PasswordEditActivity::class.java)
                startActivity(intent)
            }
        }

        profileViewModel.logOutSuccess.observe(viewLifecycleOwner){
            if(it){
                mActivity.finish()
                val intent: Intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

        profileViewModel.openGallery.observe(viewLifecycleOwner){
            if(it){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                photoResult.launch(intent)
            }
        }

        profileViewModel.imageUpload.observe(viewLifecycleOwner){
            if(it){
                if(profileViewModel.profileImgURL.value != ""){
                    Glide.with(this).load(profileViewModel.profileImgURL.value).into(binding.imgProfile)
                }
            }
        }

        profileViewModel.accountError.observe(viewLifecycleOwner){
            when(it){
                0->Toast.makeText(activity,"Error : 계정 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                1->Toast.makeText(activity,"Error : 문서가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        profileViewModel.nickNameEditActivityStart.observe(viewLifecycleOwner){
            if(it){
                val intent: Intent = Intent(requireActivity(), NickNameActivity::class.java)
                intent.putExtra("nickname",profileViewModel.profileNickname.value)
                intent.putExtra("statusmessage",profileViewModel. profileStatusMessage.value)
                startActivityForResult(intent, NICKNAME_REQUEST_CODE)
            }
        }

        profileViewModel.editProfileData.observe(viewLifecycleOwner){
            val menuItem = binding.toolbar.menu.findItem(R.id.menu_profile_edit)
            if(it){
                menuItem?.title = "저장"

                binding.btnPasswordEdit.visibility = View.VISIBLE
                binding.btnAddressEdit.visibility = View.VISIBLE
                binding.icGallery.visibility = View.VISIBLE

                binding.layoutNickname.isEnabled = true
                binding.imgProfile.isEnabled = true
                binding.layoutPassword.isEnabled = true
                binding.layoutAddress.isEnabled = true

            }else{
                menuItem?.title = "수정"

                binding.btnPasswordEdit.visibility = View.GONE
                binding.btnAddressEdit.visibility = View.GONE
                binding.icGallery.visibility = View.INVISIBLE

                binding.layoutNickname.isEnabled = false
                binding.imgProfile.isEnabled = false
                binding.layoutPassword.isEnabled = false
                binding.layoutAddress.isEnabled = false
            }
        }
    }
}