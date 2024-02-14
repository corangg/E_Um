package com.example.appointment.model

import android.os.Parcel
import android.os.Parcelable
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.annotations.SerializedName

data class PrivacyDataModel(
    var email:String,
    var password:String,
    var name:String,
    var nickname:String,
    var phoneNumber:String,
    var zonecode:String,
    var address:String
)

data class AddressData(
    val addressTitledatas : String,
    val addressdatas : String,
    var titleaddress : Boolean
)

data class ProfileDataModel(
    var email : String,
    var nickname: String,
    var statusmessage: String,
    var phoneNumber: String,
    var imgURL: String
)

data class ChatInfo(
    val chatCount : Int,
    val chatRoomName : String,
)

data class EmailNicknameData(
    val email1: String?,
    val email2: String?,
    val nickname1: String?,
    val nickname2: String?,
)

data class ChatDataModel(
    var email:String,
    var time:String,
    var message:String,
    var messageCount:Int? = 0
)

data class ChatCreateData(
    val profileURL:String,
    val friendNickname:String
)

data class ChatRoomData(
    var email:String,
    var nickname:String,
    var lastMessage:String,
    var time:Long? = 0,
    val notCheckChat:Int = 0,
    val imgURL: String
)

data class ScheduleSet(
    val scheduleName:String,
    val email: String,
    val nickname: String,
    val profileImgURL: String,
    val time: String,
    val meetingPlaceAddress: String,
    val meetingPlaceKeyword: String,
    val status :String,
    val endX :String,
    val endY :String,
    val alarm : String,
    val transport : String,
    val startX: String,
    val startY: String,
    val transportTime : String,
    val myTransport : String,
    val myAlarmTime : String
)

data class ScheduleTime(
    val HH : String,
    val MM : String,
)

data class AlarmTime(
    val YYYY: Int,
    val MM: Int,
    val DD: Int,
    val hh: Int,
    val mm: Int,
)

data class StartCheckAlarmData(
    val startX: String,
    val startY : String,
    val meetingName : String
)

data class FriendRequestAlarmData(
    val email: String,
    val nickname: String,
)

//NaverKeWord
data class KeyWordResponse(
    @SerializedName("items")
    val items: List<PlaceItem>
)

data class PlaceItem(
    @SerializedName("title") val title: String,
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("mapx") val x: Double,
    @SerializedName("mapy") val y: Double,
)

data class SelectTransport(
    val imgBus : ImageView,
    val imgCar : ImageView,
    val imgWalk : ImageView,
    val textBus : TextView,
    val textCar : TextView,
    val textWalk : TextView
)

//NaverGeocode
data class GeocodingRespone(
    val status : String,
    val addresses :List<AddressXY>
)

data class AddressXY(
    val x: String,
    val y: String

)

//NaverReverseGeocode
data class ReverseGeocodingResponse(
    val status: Status,
    val results: List<Result>
)

data class Status(
    val code: Int,
    val name: String
)

data class Result(
    val name: String,
    val region: Region,
    val land: Land,
    val addition0: Addition0,
    // 필요한 다른 필드들 추가
)

data class Region(
    val area0: Area,
    val area1: Area,
    val area2: Area,
    val area3: Area
)

data class Area(
    val name: String
)

data class Land(
    val type: String,
    val number1: String,
    val number2: String,
    val name: String
)

data class Addition0(
    val value: String
)

//TMapReverseGeo
data class TMapReverseGeocodingResponse(
    val addressInfo: List<TMapAddressInfo>
)

data class TMapAddressInfo(
    val fullAddress : String,
    val buildingName : String

)

//TMapTransit
data class TransitRouteRequest(
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String,
    val searchDttm : String
)
data class PublicTransportRouteResponse(
    val metaData: MetaData
)

data class MetaData(
    val requestParameters: RequestParameters,
    val plan: Plan
)

data class RequestParameters(
    val busCount: Int,
    val expressbusCount: Int,
    val airportCount: Int,
    val subwayCount: Int,
    val maxWalkDistance: String,
    val locale: String,
    val endY: String,
    val endX: String,
    val wideareaRouteCount: Int,
    val subwayBusCount: Int,
    val startY: String,
    val startX: String,
    val ferryCount: Int,
    val trainCount: Int,
    val reqDttm: String
)

data class Plan(
    val itineraries: List<Itinerary>
)

data class Itinerary(
    val fare: Fare,
    val walkDistance: Int,
    val totalTime: Int,
    val legs: List<Leg>,
    val walkTime: Int,
    val transferCount: Int,
    val totalDistance: Int,
    val pathType: Int
)

data class Fare(
    val regular: RegularFare
)

data class RegularFare(
    val totalFare: Int,
    val currency: Currency
)

data class Currency(
    val symbol: String,
    val currency: String,
    val currencyCode: String
)

data class Leg(
    val mode: String,
    val sectionTime: Int,
    val distance: Int,
    val start: Location,
    val end: Location,
    val steps: List<Step>? = null,
    val routeColor: String? = null,
    val route: String? = null,
    val passStopList: PassStopList? = null,
    val type: Int? = null,
    val passShape: PassShape? = null
)

data class Location(
    val name: String,
    val lon: Double,
    val lat: Double
)

data class Step(
    val streetName: String,
    val distance: Int,
    val description: String,
    val linestring: String
)

data class PassStopList(
    val stationList: List<Station>
)

data class Station(
    val index: Int,
    val stationName: String,
    val lon: String,
    val lat: String,
    val stationID: Int
)

data class PassShape(
    val linestring: String
)

//TMapCar
data class CarRouteRequest(
    val startX : Double,
    val startY : Double,
    val endX : Double,
    val endY : Double,
    val gpsTime : String
)

data class CarRouteResponse(
    val type : String,
    val features : List<Features>//같은거 써서 괜춘할듯?
)

//TMapWalk
data class WalkRouteRequest(
    val startX : Double,
    val startY : Double,
    val endX : Double,
    val endY : Double,
    val startName : String,
    val endName : String
)

data class WalkRouteResponse(
    val type : String,
    val features : List<Features>
)

data class Features(
    val properties : Properties
)

data class  Properties(
    val totalTime : Int
)

//TMapDistance
data class DistanceRouteResponse(
    val distanceInfo : DistanceInfo
)

data class DistanceInfo(
    val distance : String
)




