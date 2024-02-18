package com.example.appointment.ui.activity.profile

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.DB.AddressDBHelper
import com.example.appointment.R
import com.example.appointment.data.RequestCode
import com.example.appointment.data.Utils.Companion.auth
import com.example.appointment.databinding.ActivityProfileAddressBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.ui.adapter.AddressListAdapter
import com.example.appointment.viewmodel.profile.EditAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAddressActivity : BaseActivity<ActivityProfileAddressBinding>(), AddressListAdapter.OnItemClickListener {

    val viewmodel : EditAddressViewModel by viewModels()
    lateinit var adapter: AddressListAdapter

    override fun layoutResId(): Int {
        return R.layout.activity_profile_address
    }

    override fun initializeUI() {
        binding.viewmodel = viewmodel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewmodel.readDB(intent,this)

        binding.recycleAddress.layoutManager = LinearLayoutManager(this)
        adapter = AddressListAdapter(viewmodel.addressDataList.value!!,this)
        binding.recycleAddress.adapter=adapter
        binding.recycleAddress.addItemDecoration(
            DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_address,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =when(item.itemId){
        R.id.menu_add ->{
            viewmodel.selectMenuAdd()
            true
        }
        R.id.menu_edit->{
            viewmodel.seledtMenuEdit()
            true
        }else-> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewmodel.handleActivityResult(requestCode,resultCode,data)
    }

    override fun onItemClickDelete(position: Int) {
        val dbHelper = AddressDBHelper(this, auth.currentUser?.email)
        viewmodel.clickDeleteItem(position,dbHelper)
    }

    override fun onItemClickTitle(address: String) {
        viewmodel.clickTitleItem(address)
    }

    override fun setObserve(){
        viewmodel.titleAddress.observe(this){
            val intent = Intent()
            intent.putExtra("titleAddress",it)
            setResult(Activity.RESULT_OK, intent)
        }

        viewmodel.startAddAddress.observe(this){
            if(it){
                val intent = Intent(this, AddAdressActivity::class.java)
                intent.putExtra("email",auth.currentUser!!.email)
                startActivityForResult(intent, RequestCode.ADD_ADDRESS_REQUEST_CODE)
            }
        }

        viewmodel.addressEdit.observe(this){
            val menuItemTitle = binding.toolbar.menu.findItem(R.id.menu_edit)
            if(it){
                menuItemTitle.title = "저장"
                adapter.setItemEditable(true)
                adapter.notifyDataSetChanged()
            }else{
                menuItemTitle.title = "수정"
                adapter.setItemEditable(false)
                adapter.notifyDataSetChanged()
            }
            adapter.notifyDataSetChanged()
        }

        viewmodel.clickItem.observe(this){
            if(it){
                adapter.notifyDataSetChanged()
            }
        }

        viewmodel.addressDataList.observe(this){
            binding.recycleAddress.layoutManager = LinearLayoutManager(this)
            adapter = AddressListAdapter(viewmodel.addressDataList.value!!,this)
            binding.recycleAddress.adapter=adapter
            binding.recycleAddress.addItemDecoration(
                DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
            )
        }
    }
}