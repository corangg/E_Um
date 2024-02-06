package com.example.appointment.ui.activity.schedule

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.appointment.ui.adapter.MapSearchResultsAdapter


import com.example.appointment.R
import com.example.appointment.data.RequestCode.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.example.appointment.databinding.ActivityScheduleMapBinding
import com.example.appointment.ui.activity.AdapterActivity
import com.example.appointment.ui.adapter.OnItemClickListener
import com.example.appointment.viewmodel.schedule.ScheduleMapViewModel
import com.naver.maps.geometry.LatLng

import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource

import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView

import com.naver.maps.map.overlay.Marker


class ScheduleMapActivity : AdapterActivity<ActivityScheduleMapBinding>(),OnMapReadyCallback, OnItemClickListener{
    lateinit var adapter : MapSearchResultsAdapter
    lateinit var locationSource: FusedLocationSource
    lateinit var mapView: MapView

    val viewmodel : ScheduleMapViewModel by viewModels()
    var previousMarker: Marker? = null

    override fun layoutResId(): Int {
        return R.layout.activity_schedule_map
    }

    override fun initializeUI() {
        binding.viewmodel = viewmodel

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewmodel = viewmodel

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
        viewmodel.fnClickItem(position)
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

        naverMap.setOnMapClickListener { point, coord ->
            // 터치한 위치의 좌표
            val latitude = coord.latitude
            val longitude = coord.longitude
            val latLng = com.naver.maps.geometry.LatLng(latitude, longitude)
            val coords: String = longitude.toString()+","+latitude.toString()
            naverMap.moveCamera(CameraUpdate.scrollTo(latLng))
            fnMarkerSet(naverMap,latLng)
            viewmodel.fnClickMap(latitude,longitude,coords)
        }

        viewmodel.showSelectMarker.observe(this){
            if(it){
                if (viewmodel.searchCoordinate.value != null){

                    val currentLocation = locationSource.lastLocation
                    if (currentLocation != null) {
                        viewmodel.endX.value = viewmodel.searchCoordinate.value!!.longitude.toString()
                        viewmodel.endY.value = viewmodel.searchCoordinate.value!!.latitude.toString()
                    }

                    binding.recycleSearch.visibility = View.GONE
                    naverMap.moveCamera(CameraUpdate.scrollTo(viewmodel.searchCoordinate.value!!))

                    fnMarkerSet(naverMap, viewmodel.searchCoordinate.value!!)
                }
            }
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
        if(viewmodel.showKeywordList.value == true){
            binding.recycleSearch.visibility = View.GONE
            viewmodel.showKeywordList.value = false
            viewmodel.searchKewordAndAddress.value = ""
        }else if (binding.tabAddress.visibility ==View.VISIBLE){
            viewmodel.showSelectKeywordTab.value = false
        }else{
            super.onBackPressed()
        }
    }

    private fun fnMarkerSet(naverMap: NaverMap,position: LatLng){
        previousMarker?.map = null

        val marker = Marker()
        marker.position = position
        marker.map = naverMap

        previousMarker = marker
    }

    override fun setObserve(){
        viewmodel.showKeywordList.observe(this){//왜 이거 이럼?보이는거에 따라? 리스트변경에 따르는게 더 좋지 않을까?
            if(it){
                binding.recycleSearch.visibility = View.VISIBLE

                setAdapter(binding.recycleView,MapSearchResultsAdapter(viewmodel.searchKewordList.value!!,this),viewmodel.searchKewordList.value!!,true)
            }
        }

        viewmodel.showSelectKeywordTab.observe(this){
            if(it){
                binding.tabAddress.visibility = View.VISIBLE
            }else{
                binding.tabAddress.visibility = View.GONE
            }
        }

        viewmodel.mapActivityEnd.observe(this){
            val intent = Intent()
            intent.putExtra("endX", viewmodel.endX.value)
            intent.putExtra("endY", viewmodel.endY.value)
            intent.putExtra("address", viewmodel.selectAddress.value)
            intent.putExtra("keywordName",viewmodel.kewordName.value)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}