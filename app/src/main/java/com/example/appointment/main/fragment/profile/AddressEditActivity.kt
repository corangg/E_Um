package com.example.appointment.main.fragment.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.AddressAdapter
import com.example.appointment.AddressDBHelper
import com.example.appointment.R
import com.example.appointment.databinding.ActivityProfileAddressBinding
import com.example.appointment.login.Login_Viewmodel
import com.example.appointment.main.MainViewmodel

class AddressEditActivity : AppCompatActivity(),AddressAdapter.OnItemClickListener {
    lateinit var binding: ActivityProfileAddressBinding

    val mainViewmodel : MainViewmodel by viewModels()
    val loginViewmodel: Login_Viewmodel by viewModels()

    var addressTitledatas: MutableList<String>? = null
    var addressdatas: MutableList<String>? = null
    var mainaddress: MutableList<Boolean>? = null

    lateinit var adapter: AddressAdapter
    var email:String? = null
    var mainAddress:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile_address)
        binding.viewmodel = mainViewmodel
        binding.lifecycleOwner = this
        setObserve()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mainViewmodel.profileAddress.value = intent.getStringExtra("mainaddress")

        addressdatas = mutableListOf<String>()
        addressTitledatas = mutableListOf<String>()
        mainaddress = mutableListOf<Boolean>()

        binding.recycleAddress.layoutManager = LinearLayoutManager(this)
        adapter = AddressAdapter(addressTitledatas,addressdatas,mainaddress,this)
        binding.recycleAddress.adapter=adapter
        binding.recycleAddress.addItemDecoration(
            DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        )

        email = intent.getStringExtra("email")
        mainAddress = intent.getStringExtra("mainaddress")

        val db = AddressDBHelper(this,email).readableDatabase
        loginViewmodel.email
        val cursor = db.rawQuery("select * from ADDRESS_TB",null)
        //cursor.moveToFirst()
        cursor.run{
            while (moveToNext()){
                addressTitledatas?.add(cursor.getString(1))
                addressdatas?.add(cursor.getString(2))
                if(mainAddress==cursor.getString(2)){
                    mainaddress?.add(true)
                }else{
                    mainaddress?.add(false)
                }
            }
        }
        db.close()
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
            val intent = Intent(this, AddAdressActivity::class.java)
            intent.putExtra("email",email)
            startActivityForResult(intent,10)

           true
        }
        R.id.menu_edit->{

            if(item.title == "수정") {
                item.title = "저장"
                adapter.setItemEditable(true)
                adapter.notifyDataSetChanged()

            }else{
                item.title = "수정"
                adapter.setItemEditable(false)
                adapter.notifyDataSetChanged()
            }

            adapter.notifyDataSetChanged()
            true
        }else-> super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==10 && resultCode== Activity.RESULT_OK){
            data!!.getStringExtra("addressName")?.let{
                addressTitledatas?.add(it)
                adapter.notifyDataSetChanged()
            }
            data!!.getStringExtra("address")?.let{
                addressdatas?.add(it)
                adapter.notifyDataSetChanged()
            }
            mainaddress?.add(false)
        }
    }


    override fun onItemClickDelete(position: Int) {
        mainViewmodel.fnAddressDelete(position.toLong())
        adapter.notifyDataSetChanged()
    }

    override fun onItemClickTitle(address: String) {
        mainViewmodel.profileAddress.value = address
        adapter.notifyDataSetChanged()
    }



    fun setObserve(){
        mainViewmodel.profileAddress.observe(this){
            val intent = Intent()
            intent.putExtra("titleAddress",it)
            setResult(Activity.RESULT_OK, intent)
        }

    }
}