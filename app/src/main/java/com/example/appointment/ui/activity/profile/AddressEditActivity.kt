package com.example.appointment.ui.activity.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.ui.adapter.AddressListAdapter
import com.example.appointment.AddressDBHelper
import com.example.appointment.FirebaseCertified.Companion.email
import com.example.appointment.R
import com.example.appointment.databinding.ActivityProfileAddressBinding
import com.example.appointment.model.AddressData
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.viewmodel.logIn.Login_Viewmodel
import com.example.appointment.viewmodel.MainViewmodel

/*
class AddressEditActivity : BaseActivity<ActivityProfileAddressBinding>(), AddressListAdapter.OnItemClickListener {

    val viewModel: MainViewmodel by viewModels()
    var addressList : MutableList<AddressData> = mutableListOf()

    lateinit var adapter: AddressListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {


        true
        super.onCreate(savedInstanceState)
    }
    override fun layoutResId(): Int {
        return R.layout.activity_profile_address
    }

    fun test(){
        //addressList = mutableListOf()

        val email = intent.getStringExtra("email")
        val mainAddress = intent.getStringExtra("mainaddress")

        binding.recycleAddress.layoutManager = LinearLayoutManager(this)
        adapter = AddressListAdapter(addressList,this)
        adapter.setList(addressList)
        binding.recycleAddress.adapter=adapter
        binding.recycleAddress.addItemDecoration(
            DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        )

        val db = AddressDBHelper(this,email).readableDatabase
        val cursor = db.rawQuery("select * from ADDRESS_TB",null)
        cursor.run{
            while (moveToNext()){
                if(mainAddress==cursor.getString(2)){
                    val addressData = AddressData(
                        cursor.getString(1),
                        cursor.getString(2),
                        true
                    )
                    addressList.add(addressData)
                }else{
                    val addressData = AddressData(
                        cursor.getString(1),
                        cursor.getString(2),
                        false
                    )
                    addressList.add(addressData)
                }
            }
        }
        db.close()

    }

    override fun initializeUI() {
        binding.viewmodel = viewModel
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        test()
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
            var addressTitledatas :String = ""
            var addressdatas :String = ""
            data!!.getStringExtra("addressName")?.let{
                addressTitledatas = it
            }
            data!!.getStringExtra("address")?.let{
                addressdatas = it
            }

            val addressData = AddressData(
                addressTitledatas,
                addressdatas,
                false
            )
            addressList.add(addressData)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onItemClickDelete(position: Int) {
        viewModel.fnAddressDelete(position.toLong())
        adapter.notifyItemRemoved(position)
        addressList[position]
        //addressTitledatas
        //지워서 디비값은 바뀐는데 리스트는 그대로임

        //test()
        //adapter.notifyDataSetChanged()//필요한가?
    }

    override fun onItemClickTitle(address: String) {
        viewModel.profileAddress.value = address
        adapter.notifyDataSetChanged()

    }

    override fun setObserve(){
        viewModel.profileAddress.observe(this){
            val intent = Intent()
            intent.putExtra("titleAddress",it)
            setResult(Activity.RESULT_OK, intent)
        }

    }
}*/
class AddressEditActivity : AppCompatActivity(), AddressListAdapter.OnItemClickListener {
    lateinit var binding: ActivityProfileAddressBinding

    //val mainViewmodel : MainViewmodel by viewModels()
    val mainViewmodel : MainViewmodel by viewModels()
    val loginViewmodel: Login_Viewmodel by viewModels()

    var addressTitledatas: MutableList<String>? = null
    var addressdatas: MutableList<String>? = null
    var mainaddress: MutableList<Boolean>? = null

    lateinit var adapter: AddressListAdapter
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
        adapter = AddressListAdapter(addressTitledatas,addressdatas,mainaddress,this)
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