package com.example.appointment


import com.example.appointment.model.CarRouteRequest
import com.example.appointment.model.CarRouteResponse
import com.example.appointment.model.DistanceRouteResponse
import com.example.appointment.model.KeyWordResponse
import com.example.appointment.model.GeocodingRespone
import com.example.appointment.model.ReverseGeocodingResponse
import com.example.appointment.model.TransitRouteRequest
import com.example.appointment.model.PublicTransportRouteResponse
import com.example.appointment.model.TMapReverseGeocodingResponse
import com.example.appointment.model.WalkRouteRequest
import com.example.appointment.model.WalkRouteResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NaverKeWord{
    @GET("v1/search/local.json")
    fun getKeword(
        @Header("X-Naver-Client-Id") apiKeyID: String,
        @Header("X-Naver-Client-Secret") apiKey: String,
        @Query("query") query: String,
        @Query("display") display: Int
    ): Call<KeyWordResponse>
}

interface NaverGeocode{
    @GET("map-geocode/v2/geocode")
    fun getGeocode(
        @Header("X-NCP-APIGW-API-KEY-ID") apiKeyID: String,
        @Header("X-NCP-APIGW-API-KEY") apiKey: String,
        @Query("query") query: String,
    ): Call<GeocodingRespone>
}

interface NaverReverseGeocode{
    @GET("map-reversegeocode/v2/gc")
    fun getReverseGeocode(
        @Header("X-NCP-APIGW-API-KEY-ID") apiKeyID: String,
        @Header("X-NCP-APIGW-API-KEY") apiKey: String,
        @Query("coords") coords: String,
        @Query("output") output: String,
        @Query("orders") orders: String
        ): Call<ReverseGeocodingResponse>
}

/*interface TMapReverseGeocode{
    @Headers("accept: application/json")
    fun getReverseGeocode(
        @Header("appKey") appKey: String,
        @Query("version") version : String,
        @Query("lat") lat : String,
        @Query("lon") lon : String,
        @Query("callback") callback : String,

    ): Call<TMapReverseGeocodingResponse>
}*/


interface TMapPublicTransportRoute {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/transit/routes")
    fun getPublicTransportRoute(
        @Header("appKey") appKey: String,
        @Body request: TransitRouteRequest
    ): Call<PublicTransportRouteResponse>
}

interface TMapCarRoute {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/tmap/routes")
    fun getCarRoute(
        @Header("appKey") appKey: String,
        @Query("version") version : String,
        @Query("callback") callback : String,
        @Body request: CarRouteRequest
    ): Call<CarRouteResponse>
}

interface TMapWalkRoute {
    @Headers("accept: application/json", "content-type: application/json")
    @POST("/tmap/routes/pedestrian")
    fun getWalkRoute(
        @Header("appKey") appKey: String,
        @Query("version") version : String,
        @Query("callback") callback : String,
        @Body request: WalkRouteRequest
    ): Call<WalkRouteResponse>
}

interface TMapDistanceRoute {
    @Headers("accept: application/json")
    @POST("/tmap/routes/distance")
    fun getDistanceRoute(
        @Header("appKey") appKey: String,
        @Query("version") version : String,
        @Query("callback") callback : String,
        @Query("startX") startX : String,
        @Query("startY") startY : String,
        @Query("endX") endX : String,
        @Query("endY") endY : String,
    ): Call<DistanceRouteResponse>
}

