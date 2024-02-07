package com.example.appointment.viewmodel.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.appointment.apiservice.APIData
import com.example.appointment.apiservice.NaverKeWord
import com.example.appointment.apiservice.NaverReverseGeocode
import com.example.appointment.data.Utils
import com.example.appointment.model.KeyWordResponse
import com.example.appointment.model.PlaceItem
import com.example.appointment.model.ReverseGeocodingResponse
import com.example.appointment.viewmodel.BaseViewModel
import com.naver.maps.geometry.LatLng
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ScheduleMapViewModel(application: Application) : BaseViewModel(application){
    var searchKewordAndAddress : MutableLiveData<String?> = MutableLiveData("")
    var showKeywordList : MutableLiveData<Boolean> = MutableLiveData(false)
    var searchKewordList : MutableLiveData<MutableList<PlaceItem>> = MutableLiveData()
    var kewordName : MutableLiveData<String?> = MutableLiveData("")
    var selectAddress : MutableLiveData<String?> = MutableLiveData("")
    var selectRoadAddress : MutableLiveData<String?> = MutableLiveData("")
    var mapActivityEnd : MutableLiveData<Unit> = MutableLiveData()
    var searchCoordinate : MutableLiveData<LatLng> = MutableLiveData(null)
    var showSelectMarker : MutableLiveData<Boolean> = MutableLiveData(false)
    var showSelectKeywordTab : MutableLiveData<Boolean> = MutableLiveData(false)
    var endX:MutableLiveData<String> = MutableLiveData("")
    var endY:MutableLiveData<String> = MutableLiveData("")

    fun fnClickMap(latitude: Double, longitude: Double, coords: String){
        endX.value = longitude.toString()
        endY.value = latitude.toString()

        fnReverseGeo(coords)
    }

    fun fnClickItem(position:Int){
        val latLng = com.naver.maps.geometry.LatLng(searchKewordList.value!![position].y*0.0000001,searchKewordList.value!![position].x*0.0000001)
        val searchName : String = searchKewordList.value!![position].title.replace("<b>","").replace("</b>","")
        fnSelectKewordTabDataSet(latLng,searchName,searchKewordList.value!![position].roadAddress,searchKewordList.value!![position].address)
    }

    fun fnSelectKewordTabDataSet(mapPoint: LatLng, name: String, roadAddress:String, address:String){
        searchCoordinate.value = mapPoint
        kewordName.value = name
        selectAddress.value = address
        selectRoadAddress.value = roadAddress
        showSelectMarker.value = true
        showKeywordList.value = false
        searchKewordAndAddress.value = ""
        showSelectKeywordTab.value = true
    }

    fun fnSearchKeyword(keyword: String){
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
                fnSearchKewordList(it)
                showKeywordList.value = true
            }
        }
    }

    fun fnSearchKewordList(searchResult: KeyWordResponse?){
        val searchList : MutableList<PlaceItem> = mutableListOf()
        for (document in searchResult!!.items) {
            searchList.add(document)
        }
        searchKewordList.value = searchList
    }

    fun fnEndMapActivity(){
        mapActivityEnd.value = Unit
    }





    fun fnReverseGeo(coords:String){
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

                kewordName.value =""//addition0.value값이 안들어옴 나중에 확인 해보자
                showSelectKeywordTab.value = true
            }
        }
    }

    fun fnReverseGeoAddressSet(body: ReverseGeocodingResponse?){
        var address : String = ""
        if(body!!.results[0] != null){
            if (body.results[0].land.number2==""){
                address = body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1
            }else{
                address = body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1+"-"+body.results[0].land.number2
            }
        }

        selectAddress.value = address
    }

    fun fnReverseGeoRoadAddressSet(body: ReverseGeocodingResponse?){
        var roadAddress : String = ""

        if(body!!.results.size == 1){

        }else if(body!!.results.size == 2){
            if(body!!.results[1] != null){
                roadAddress = body.results[1].region.area1.name+body.results[1].region.area2.name+body.results[1].land.name+body.results[1].land.number1
            }
        }

        selectRoadAddress.value = roadAddress
    }

}