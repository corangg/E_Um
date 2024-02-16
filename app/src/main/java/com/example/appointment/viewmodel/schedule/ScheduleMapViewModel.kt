package com.example.appointment.viewmodel.schedule

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.appointment.apiservice.APIData
import com.example.appointment.apiservice.NaverKeWord
import com.example.appointment.apiservice.NaverReverseGeocode
import com.example.appointment.model.KeyWordResponse
import com.example.appointment.model.PlaceItem
import com.example.appointment.model.ReverseGeocodingResponse
import com.example.appointment.repository.ScheduleMapRepository
import com.example.appointment.viewmodel.BaseViewModel
import com.naver.maps.geometry.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class ScheduleMapViewModel @Inject constructor(
    application: Application,
    private val scheduleMapRepository: ScheduleMapRepository) : BaseViewModel(application){
    val searchKewordAndAddress : MutableLiveData<String> = MutableLiveData("")
    val kewordName : MutableLiveData<String> = MutableLiveData("")
    val selectAddress : MutableLiveData<String> = MutableLiveData("")
    val selectRoadAddress : MutableLiveData<String> = MutableLiveData("")

    val showKeywordList : MutableLiveData<Boolean> = MutableLiveData(false)
    val showSelectMarker : MutableLiveData<Boolean> = MutableLiveData(false)
    val showSelectKeywordTab : MutableLiveData<Boolean> = MutableLiveData(false)

    val searchCoordinate : MutableLiveData<LatLng> = MutableLiveData(null)
    val searchKewordList : MutableLiveData<MutableList<PlaceItem>> = MutableLiveData()
    val mapActivityEnd : MutableLiveData<Unit> = MutableLiveData()

    var endX : String = ""
    var endY : String = ""

    fun onClickMap(latitude: Double, longitude: Double, coords: String){
        endX = longitude.toString()
        endY = latitude.toString()

        reverseGeo(coords)
    }

    fun onClickItem(position:Int){
        val latLng = scheduleMapRepository.setLatLng(searchKewordList.value!![position])
        val searchName = scheduleMapRepository.setSearchName(searchKewordList.value!![position])
        val roadAddress = searchKewordList.value!![position].roadAddress
        val address = searchKewordList.value!![position].address
        selectKewordTabDataSet(latLng,searchName,roadAddress,address)
    }

    fun selectKewordTabDataSet(mapPoint: LatLng, name: String, roadAddress:String, address:String){
        searchCoordinate.value = mapPoint
        kewordName.value = name
        selectAddress.value = address
        selectRoadAddress.value = roadAddress
        showSelectMarker.value = true
        showKeywordList.value = false
        searchKewordAndAddress.value = ""
        showSelectKeywordTab.value = true
    }

    fun searchKeyword(keyword: String){
        val retrofit = Retrofit.Builder()
            .baseUrl(APIData.KEWORD_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val naverapi = retrofit.create(NaverKeWord::class.java)
        val call = naverapi.getKeword(
            APIData.KEWORD_CLIENT_ID,
            APIData.KEWORD_API_KEY,keyword,5)

        utils.getRetrofitData(call){
            if(it!=null){
                searchKewordList(it)
                showKeywordList.value = true
            }
        }
    }

    fun searchKewordList(searchResult: KeyWordResponse?){
        searchKewordList.value = scheduleMapRepository.searchKewordListSet(searchResult)
    }

    fun endMapActivity(){
        mapActivityEnd.value = Unit
    }

    fun reverseGeo(coords:String){
        val retrofit = Retrofit.Builder()
            .baseUrl(APIData.NAVER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val naverapi = retrofit.create(NaverReverseGeocode::class.java)
        val call = naverapi.getReverseGeocode(
            APIData.NAVER_CLIENT_ID,
            APIData.NAVER_API_KEY,coords,"json","addr,roadaddr")

        utils.getRetrofitData(call){
            if(it!=null){
                fnReverseGeoAddressSet(it)
                fnReverseGeoRoadAddressSet(it)
                kewordName.value =""
                showSelectKeywordTab.value = true
            }
        }
    }

    fun fnReverseGeoAddressSet(body: ReverseGeocodingResponse?){
        selectAddress.value = scheduleMapRepository.reverseGeoAddressSet(body)
    }

    fun fnReverseGeoRoadAddressSet(body: ReverseGeocodingResponse?){
        selectRoadAddress.value = scheduleMapRepository.reverseGeoRoadAddressSet(body)
    }
}