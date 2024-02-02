package com.example.appointment.ui.activity.schedule

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appointment.ui.adapter.MapSearchResultsAdapter


import com.example.appointment.R
import com.example.appointment.databinding.ActivityScheduleMapBinding
import com.example.appointment.ui.activity.BaseActivity
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.viewmodel.MainViewmodel
import com.naver.maps.geometry.LatLng

import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView

import com.naver.maps.map.overlay.Marker


class ScheduleMapActivity : BaseActivity<ActivityScheduleMapBinding>(),OnMapReadyCallback, OnItemClickListener{
    lateinit var adapter : MapSearchResultsAdapter
    lateinit var locationSource: FusedLocationSource
    lateinit var mapView: MapView

    val mainViewmodel : MainViewmodel by viewModels()
    var previousMarker: Marker? = null

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    override fun layoutResId(): Int {
        return R.layout.activity_schedule_map
    }

    override fun initializeUI() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = mainViewmodel

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onItemClick(position: Int) {
        val latLng = com.naver.maps.geometry.LatLng(mainViewmodel.searchKewordList.value!![position].y*0.0000001,mainViewmodel.searchKewordList.value!![position].x*0.0000001)
        val searchName : String = mainViewmodel.searchKewordList.value!![position].title.replace("<b>","").replace("</b>","")
        mainViewmodel.fnSelectKewordTabDataSet(latLng,searchName,mainViewmodel.searchKewordList.value!![position].roadAddress,mainViewmodel.searchKewordList.value!![position].address)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapView.getMapAsync(this)
            } else {
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun fnMarkerSet(naverMap: NaverMap,position: LatLng){
        previousMarker?.map = null

        val marker = Marker()
        marker.position = position
        marker.map = naverMap

        previousMarker = marker
    }

    override fun onMapReady(naverMap: NaverMap) {
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            naverMap.locationSource = locationSource
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        mainViewmodel.showSelectMarker.observe(this){
            if(it){
                if (mainViewmodel.searchCoordinate.value != null){

                    val currentLocation = locationSource.lastLocation
                    if (currentLocation != null) {
                        mainViewmodel.endX.value = mainViewmodel.searchCoordinate.value!!.longitude.toString()
                        mainViewmodel.endY.value = mainViewmodel.searchCoordinate.value!!.latitude.toString()
                    }

                    binding.recycleSearch.visibility = View.GONE
                    naverMap.moveCamera(CameraUpdate.scrollTo(mainViewmodel.searchCoordinate.value!!))

                    fnMarkerSet(naverMap, mainViewmodel.searchCoordinate.value!!)
                }
            }
        }


        naverMap.setOnMapClickListener { point, coord ->
            // 터치한 위치의 좌표
            val latitude = coord.latitude
            val longitude = coord.longitude

            val latLng = com.naver.maps.geometry.LatLng(latitude, longitude)

            naverMap.moveCamera(CameraUpdate.scrollTo(latLng))

            fnMarkerSet(naverMap,latLng)

            val coords:String = longitude.toString()+","+latitude.toString()

            /*val currentLocation = locationSource.lastLocation//이게 널이어서 그런건데
            if (currentLocation != null) {

            }*/
            mainViewmodel.endX.value = longitude.toString()
            mainViewmodel.endY.value = latitude.toString()

            mainViewmodel.fnReverseGeo(coords)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(mainViewmodel.showKeywordList.value == true){
            binding.recycleSearch.visibility = View.GONE
            mainViewmodel.showKeywordList.value = false
            mainViewmodel.searchKewordAndAddress.value = ""
        }else if (binding.tabAddress.visibility ==View.VISIBLE){
            mainViewmodel.showSelectKeywordTab.value = false
        }else{
            super.onBackPressed()
        }
    }

    override fun setObserve(){
        mainViewmodel.showKeywordList.observe(this){//왜 이거 이럼?보이는거에 따라? 리스트변경에 따르는게 더 좋지 않을까?
            if(it){
                binding.recycleSearch.visibility = View.VISIBLE
                binding.recycleView.layoutManager = LinearLayoutManager(this)
                adapter = MapSearchResultsAdapter(mainViewmodel.searchKewordList.value!!,this)
                binding.recycleView.adapter = adapter
                adapter.setList(mainViewmodel.searchKewordList.value!!)
                binding.recycleView.addItemDecoration(
                    DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
                )
            }
        }

        mainViewmodel.showSelectKeywordTab.observe(this){
            if(it){
                binding.tabAddress.visibility = View.VISIBLE
            }else{
                binding.tabAddress.visibility = View.GONE
            }
        }

        mainViewmodel.mapActivityEnd.observe(this){
            if(it){
                val intent = Intent()
                intent.putExtra("endX", mainViewmodel.endX.value)
                intent.putExtra("endY", mainViewmodel.endY.value)
                intent.putExtra("address", mainViewmodel.selectAddress.value)
                intent.putExtra("keywordName",mainViewmodel.kewordName.value)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}