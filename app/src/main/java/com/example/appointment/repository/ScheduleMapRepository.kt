package com.example.appointment.repository

import com.example.appointment.model.KeyWordResponse
import com.example.appointment.model.PlaceItem
import com.example.appointment.model.ReverseGeocodingResponse
import com.naver.maps.geometry.LatLng

class ScheduleMapRepository {

    fun setLatLng(placeItem: PlaceItem) : LatLng{
        return LatLng(placeItem.y*0.0000001,placeItem.x*0.0000001)
    }

    fun setSearchName(placeItem: PlaceItem) : String{
        return placeItem.title.replace("<b>","").replace("</b>","")
    }

    fun searchKewordListSet(searchResult: KeyWordResponse?) : MutableList<PlaceItem>{
        val searchList : MutableList<PlaceItem> = mutableListOf()
        for (document in searchResult!!.items) {
            searchList.add(document)
        }
        return  searchList
    }

    fun reverseGeoAddressSet(body: ReverseGeocodingResponse?) : String{
        return if(body!!.results[0] != null){
            if(body.results[0].land.number2==""){
                body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1
            }else{
                body.results[0].region.area1.name+body.results[0].region.area2.name+body.results[0].region.area3.name+body.results[0].land.number1+"-"+body.results[0].land.number2
            }
        }else{
            ""
        }
    }

    fun reverseGeoRoadAddressSet(body: ReverseGeocodingResponse?) : String{
        return if(body!!.results.size == 2){
            if(body!!.results[1] != null){
                body.results[1].region.area1.name+body.results[1].region.area2.name+body.results[1].land.name+body.results[1].land.number1
            }else{
                ""
            }
        }else{
            ""
        }
    }
}