
package com.exflyer.oddiad.network.api;


import com.google.gson.JsonObject;
import com.exflyer.oddiad.network.model.bottomad.BottomAdModel;
import com.exflyer.oddiad.network.model.common.CommonResponse;
import com.exflyer.oddiad.network.model.devicecreate.DeviceCreateModel;
import com.exflyer.oddiad.network.model.mainad.MainAdModel;
import com.exflyer.oddiad.network.model.sidead.SideAdModel;
import com.exflyer.oddiad.network.model.weather.WeatherModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OddiAdApiService {


    //디바이스 생성
    @POST("device/create")
    @Headers({ "Content-Type:application/json"})
    Call<DeviceCreateModel> onDeviceCreate(@Body JsonObject param);

    //FCM 수신 체크
    @POST("device/fcm/response")
    @Headers({ "Content-Type:application/json"})
    Call<CommonResponse> onPushResponse(@Body JsonObject param);

    //Main 광고 요청 API
    @POST("content/get/main")
    @Headers({ "Content-Type:application/json"})
    Call<MainAdModel> getMainAD(@Body JsonObject param);

    //우측화면 광고 요청 API
    @POST("content/get/side")
    @Headers({ "Content-Type:application/json"})
    Call<SideAdModel> getSidemAD(@Body JsonObject param);

    //하단화면 광고 요청 API
    @POST("content/get/bottom")
    @Headers({ "Content-Type:application/json"})
    Call<BottomAdModel> getBottomAD(@Body JsonObject param);

    //위치기반 날씨 데이터 요청 처리
    @POST("content/get/weather")
    @Headers({ "Content-Type:application/json"})
    Call<WeatherModel> getLocationWeather(@Body JsonObject param);

    //디바이스 상태
    @POST("device/state")
    @Headers({ "Content-Type:application/json"})
    Call<CommonResponse> onDeviceState(@Body JsonObject param);

    //컨텐츠 재생 상태
    @POST("content/state")
    @Headers({ "Content-Type:application/json"})
    Call<CommonResponse> onContentState(@Body JsonObject param);

    //비정상 종료 체크
    @POST("device/exception")
    @Headers({ "Content-Type:application/json"})
    Call<CommonResponse> onDeviceException(@Body JsonObject param);

}
